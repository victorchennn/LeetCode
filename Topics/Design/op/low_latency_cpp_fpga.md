# Low-Latency Trading System：C++、Market Data Receiver 与 FPGA

![Low-Latency Trading
Architecture](low_latency_trading_architecture.png)

这张图展示了一个典型的 low-latency trading system：**Exchange
发出市场数据 → 系统接收并处理 → Strategy 做决策 → Order 发回
Exchange**。对于普通策略，这条链路主要运行在 CPU/C++ 上；对于极端
latency-sensitive 的策略，可以把最后的快速触发路径放到 FPGA。

## 1. Exchange：市场数据从哪里来，订单发到哪里

左边的建筑可以理解成 Exchange，例如 NASDAQ /
NYSE。交易所一方面向市场参与者发送 Market Data，另一方面接收交易公司的
Buy / Sell / Cancel Order。

``` text
Exchange
   │
   ├── Market Data ─────────────→ Trading System
   │
   └── ←──────────────────────── Orders
```

图中的 `10Gb/s Multicast` 表示交易所可以通过高速网络使用 UDP Multicast
分发市场数据。Multicast
的核心思想是：交易所发送一份数据，多个订阅者都可以接收，而不是分别为每家公司建立一条独立的数据发送路径。

``` text
                 ┌── Firm A
Exchange ────────┼── Firm B
                 ├── Firm C
                 └── Firm D
```

`10Gb/s` 是网络链路速率，不代表市场数据一直占满 10Gb/s。

## 2. Market Data Receiver：把网络 Packet 变成 Strategy 能使用的数据

Exchange 发来的不是 C++ object，而是网络上的 binary
packet。概念上可能类似：

``` text
[Ethernet Header]
[IP Header]
[UDP Header]
[Exchange Protocol Message]

Exchange Message:
type       = ADD_ORDER
symbol     = AAPL
side       = BUY
price      = 22010
quantity   = 100
sequence   = 82736192
```

Market Data Receiver 的工作大致是：

``` text
NIC 收到 packet
      ↓
网络协议栈 / packet processing
      ↓
解析 exchange protocol
      ↓
检查 sequence number
      ↓
decode market message
      ↓
更新本地 market state / order book
      ↓
把 price / market update 交给 Strategy
```

所以 NIC、UDP、Multicast、Socket、Kernel、Kernel Bypass
等网络概念，本质上都处于 **Exchange → Market Data Receiver**
这条路径上。

## 3. Strategy A / B / C：普通 C++ Strategy

Market Data Receiver 解析行情后，把更新交给不同 Strategy：

``` text
Exchange
   ↓
UDP Multicast
   ↓
Market Data Receiver
   ↓
Prices / Market Updates
   ├──→ Strategy A
   ├──→ Strategy B
   └──→ Strategy C
```

Strategy 根据市场状态做计算，例如：

``` cpp
void Strategy::onMarketUpdate(const MarketUpdate& update) {
    if (shouldBuy(update)) {
        sendOrder(...);
    }
}
```

如果策略决定交易：

``` text
Market Update
     ↓
Strategy calculation
     ↓
BUY / SELL decision
     ↓
Order
     ↓
Exchange
```

Low latency 的意义就在这里：如果多个交易者同时发现一个短暂机会，更快完成
`receive → calculate → send` 的系统可能更早到达 Exchange。

## 4. 为什么有多个 Strategy？

不同策略对 latency
的要求不同。一个复杂统计模型可能允许几百微秒甚至毫秒级计算，而非常简单的套利或
market-making trigger 可能极度关注微秒级 latency。

因此不是整个 Trading System 都必须使用最激进的优化。普通 Strategy
可以使用正常的现代 C++ 数据结构和架构；真正的 hot path
才可能进一步使用：

``` text
CPU pinning
preallocated memory
cache-friendly data layout
single-writer architecture
lock-free communication
busy polling
kernel bypass
FPGA
```

核心原则是：**只优化真正 latency-sensitive 的路径。**

## 5. FPGA 是什么角色？

FPGA 可以理解成一种可以根据需求配置成专门数字电路的硬件。CPU 执行：

``` cpp
if (price <= triggerPrice) {
    sendOrder();
}
```

需要经过 CPU instruction execution 以及软件/network stack 等步骤。而
FPGA 可以把简单逻辑直接做成硬件 pipeline：

``` text
packet
  ↓
extract price
  ↓
compare trigger
  ↓
construct order packet
  ↓
send
```

因此对于非常简单、非常稳定、同时极度 latency-sensitive 的逻辑，FPGA
可以比普通 CPU software path 更快。

但 FPGA 并不是 C++ 的全面替代品。它通常更难开发、修改和
debug，也不适合频繁变化的复杂策略。因此常见思想是：

``` text
复杂、经常变化的计算
        ↓
       C++

简单、稳定、极度 latency-sensitive 的 fast path
        ↓
      FPGA
```

## 6. FPGA 可以从哪里获得 Market Data？

FPGA 并不只有一种使用方式。

### 方式 A：FPGA 直接接收 Exchange Multicast

这是追求最低 latency 的方式之一：

``` text
Exchange
   │
   │ UDP Multicast
   ▼
 FPGA
   │
   ├── parse market data
   ├── check trigger
   └── construct order
   │
   ▼
Exchange
```

这样 market data 不需要先经过 CPU 上的 Market Data Receiver 和
Strategy，再回到 FPGA。

### 方式 B：Market Data Receiver / CPU 把 Price 或 Signal 给 FPGA

也可以设计成：

``` text
Exchange
   ↓
Market Data Receiver (C++)
   ↓
decoded price / signal
   ↓
FPGA
   ↓
Order
   ↓
Exchange
```

这种架构是可行的，但如果目标是最低可能 latency，那么 Market Data
已经绕过 CPU software path 后再进入 FPGA，fast path 的优势会减少。

## 7. 图中真正画的是：FPGA 同时有两类输入

这张图里最值得注意的是，FPGA 可以直接获得 Exchange 的 multicast，同时
Strategy D 也向 FPGA 提供一个 `Trigger Price`。

``` text
                       Exchange
                          │
                 UDP Multicast
                    ┌─────┴──────────────┐
                    ▼                    ▼
          Market Data Receiver          FPGA
                    │                    ▲
                 Prices                  │
                    │                    │ Trigger Price
                    ▼                    │
                Strategy D ──────────────┘
                                         │
                                         │ Order
                                         ▼
                                      Exchange
```

这里 Strategy D 和 FPGA 的分工可以理解成：

``` text
C++ Strategy D：
负责复杂计算
"在什么条件下应该交易？"
        ↓
计算出：
trigger_price = 199.97
quantity      = 100
side          = BUY
        ↓
把 trigger 参数配置给 FPGA
```

之后 FPGA 自己直接监听 Exchange Market Data：

``` text
Exchange Multicast

AAPL ask:
200.01
200.00
199.99
199.98
199.97  ← trigger!
          ↓
        FPGA
          ↓
     BUY 100 AAPL
          ↓
       Exchange
```

因此一个非常重要的区别是：

> **Strategy D 给 FPGA 的通常不是源源不断的实时 Market Price，而可以是
> Trigger / Execution Parameters；真正 latency-sensitive 的 Market Data
> 可以由 FPGA 直接从 Exchange Multicast 获得。**

这相当于：

``` text
CPU / C++
负责复杂思考：
"什么价格应该买？"

       ↓ configure

FPGA
负责快速反应：
"价格到了！立即发单！"
```

可以把它理解成 **software control/strategy logic + hardware fast
path**。

## 8. 完整链路

普通 C++ 路径：

``` text
                         EXCHANGE
                            │
                      UDP Multicast
                            │
                            ▼
                           NIC
                            │
                     network packet
                            │
                            ▼
                  Market Data Receiver
                            │
                    decode / order book
                            │
                            ▼
                         Strategy
                            │
                      trading decision
                            │
                            ▼
                      Order Gateway
                            │
                           NIC
                            │
                            ▼
                         EXCHANGE
```

极端 low-latency FPGA 路径：

``` text
                    C++ Strategy
                         │
                  configure trigger
                         ▼
Exchange ─────────────→ FPGA
          multicast      │
                         │ hardware fast path
                         ▼
                      Exchange
                       Order
```

## 9. 把相关概念放回整条路径

以后看到这些 low-latency
术语，可以按它们在链路中的位置理解，而不是分别死记：

``` text
Exchange
   │
   │ UDP / Multicast / Ethernet
   ▼
NIC
   │
   │ Kernel network stack
   │ Socket
   │ 或 Kernel Bypass
   ▼
Market Data Receiver
   │
   │ parsing / sequence / order book
   ▼
Strategy
   │
   │ CPU affinity
   │ cache locality
   │ memory allocation
   │ locks / lock-free
   ▼
Order Gateway
   │
   ▼
NIC
   │
   ▼
Exchange
```

FPGA 则可以把其中一部分甚至很大一部分 latency-sensitive fast path 绕开：

``` text
                ┌──── CPU / C++ complex logic
                │
                │ trigger / parameters
                ▼
Exchange ───→ FPGA ───→ Exchange
 market data              order
```

最终所有这些技术都在解决同一个问题：

> **Exchange
> 发出一个价格变化之后，我怎样尽可能快地看到它、做出决策，并把订单送回
> Exchange？**
