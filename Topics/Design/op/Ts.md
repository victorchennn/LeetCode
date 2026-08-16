# 低延迟交易系统设计：Amsterdam → Frankfurt Exchange

## 1. 题目

你是一个 Startup Trading Company 的 CTO，公司在 Amsterdam，现在要交易 Frankfurt Exchange 上的 BMW 股票。系统只有 `buy()` / `sell()` 两个功能，现有 Infra 只有交易所提供的两条连接：一条 **Info / Market Data Cable**，一条 **Order / Execution Cable**。

设计时可以按照 **物理位置 → 网络 → Market Data → Trading Engine → Order Entry → Reliability → Low Latency Optimization** 的顺序讲。

## 2. Server 放在哪里？

虽然公司在 Amsterdam，但 Trading Server 应该放在 **Frankfurt Exchange 附近的 Colocation Data Center**，而不是 Amsterdam。Low-Latency Trading 很大一部分 latency 来自物理距离，Amsterdam 到 Frankfurt 的光纤传播本身就会产生额外延迟，所以应该让真正的 Trading Engine 尽可能靠近 Exchange。

```text
Amsterdam Office
   │  WAN：部署 / Monitoring / Control
   ▼
Frankfurt Colocation
   │
   ├── Trading Server
   ├── Backup Server
   └── Fiber Cross-Connect ─── Frankfurt Exchange
```

在机房里通过 **Cross-Connect** 把自己的 Rack/NIC 和 Exchange Network 用 Fiber 直接连接。Amsterdam 主要负责 Monitoring、Deployment、Configuration 和 Kill Switch，不参与 latency-sensitive hot path。

## 3. 整体架构

```text
                  Frankfurt Exchange
                   │              ▲
       Market Data│              │Order / Execution
                   ▼              │
            +-------------+  +-------------+
            | Market Data |  |Order Gateway|
            |   Gateway   |  |             |
            +------+------+  +------+------+
                   │                ▲
                   ▼                │
                 +--------------------+
                 |   Trading Engine   |
                 | buy/sell + Risk    |
                 | Order + Position   |
                 +--------------------+
```

整个 Hot Path 就是：

```text
Exchange Market Data → NIC → Market Data Decoder → BMW Order Book
→ Strategy → Risk Check → Order Gateway → NIC → Exchange
→ ACK/FILL → Order State / Position
```

只有一只 BMW 的情况下没必要一开始引入 Kafka、Redis、Database、Microservices 等复杂组件。Hot Path 越短越好。

## 4. Market Data：UDP

如果 Exchange 提供 **UDP Multicast Market Data Feed**，优先使用 UDP。Market Data 本质上是 Exchange 把同样的数据广播给大量 Trading Firms，UDP Multicast 非常适合这种 one-to-many 场景，而且没有 TCP ACK、retransmission 和 Head-of-Line Blocking 的额外 latency。

```text
Exchange ──UDP Multicast──> NIC ──> Market Data Decoder ──> BMW Order Book
```

UDP 的问题是可能丢包，因此每个 Market Data Message 要有 **Sequence Number**。例如收到 `10001, 10002, 10003, 10005`，马上知道 `10004` 丢失，此时不能继续相信自己的 Order Book。

```cpp
if (packet.seq != expectedSeq) {
    recoverMarketData();
}
```

正常情况走 **UDP Incremental Update Fast Path**；发现 Sequence Gap 后通过 Exchange 提供的 Snapshot / Retransmission Recovery Channel 恢复数据。所以核心思想是：**UDP 负责低延迟，Sequence Number + Recovery 负责正确性。**

## 5. Order Entry / Execution：TCP

Order 和 Market Data 不一样。比如 `BUY BMW 100 @ €86.20`，必须明确知道 Exchange 有没有收到、是否 ACK、是否 Reject、成交了多少。因此如果 Exchange 提供 TCP Order Entry Protocol，可以建立长期存在的 **Persistent TCP Connection**：

```text
Trading Engine → Order Gateway ──TCP──> Exchange
                                   <── ACK / Reject / Fill
```

TCP 提供 Reliability 和 Ordering，但 Application Layer 仍然需要自己的 `OrderId`、Sequence Number 和 Session Recovery。比如订单发出去后连接断了，必须能够判断 Exchange 是否已经接受该订单，不能简单地重新发送，否则可能产生 Duplicate Order。

实际系统当然应该使用 **Exchange 官方提供的 Binary Order Entry Protocol**，而不是自己随便设计 TCP Message Format。

## 6. Buy / Sell API

对外只需要两个简单接口：

```cpp
using Price = int64_t;
using Quantity = int32_t;
using OrderId = uint64_t;

OrderId buy(Quantity qty, Price price);
OrderId sell(Quantity qty, Price price);
```

Price 不建议使用 `double`，可以直接使用 Integer Tick / Fixed Point。例如 `€86.23` 内部表示为 `8623`，避免 floating-point precision 问题。

内部可以统一成：

```cpp
enum class Side { Buy, Sell };

OrderId submitOrder(Side side, Quantity qty, Price price);

OrderId buy(Quantity qty, Price price) {
    return submitOrder(Side::Buy, qty, price);
}

OrderId sell(Quantity qty, Price price) {
    return submitOrder(Side::Sell, qty, price);
}
```

## 7. Order State 和 Position

调用：

```cpp
buy(100, 8620);
```

**不代表已经买到 100 股。** Order 发出去后可能经历：

```text
PendingNew → ACK → Live → PartialFill → Filled
                    └───────────────→ Cancelled
PendingNew → Rejected
```

比如 Exchange 返回：

```text
ACK order=123
FILL order=123 qty=30 price=8620
FILL order=123 qty=70 price=8620
```

Position 应该随着真正的 Fill 从 `0 → 30 → 100`，而不是调用 `buy()` 时直接 `+100`。因此 Trading Engine 至少需要维护 Order State 和当前 Position。

## 8. Risk Check

任何 Order 在真正发送给 Exchange 之前都必须经过 Risk Check。第一版至少检查 `maxOrderQuantity`、`maxPosition`、`maxNotional`、价格是否明显异常，以及 Kill Switch。

```cpp
bool checkRisk(Side side, int qty, Price price) {
    if (qty <= 0 || qty > maxOrderQty_) return false;

    int projected = position_ + (side == Side::Buy ? qty : -qty);
    if (std::abs(projected) > maxPosition_) return false;

    return true;
}
```

Risk Check 必须在：

```text
Strategy → Risk Check → NIC → Exchange
```

而不是 Order 已经发出去以后再检查。

## 9. 多线程设计

因为只有一只 BMW，第一版不需要复杂 Fine-Grained Locking。可以让不同线程负责不同阶段：

```text
Core 1: Market Data Thread
          ↓
       SPSC Queue
          ↓
Core 2: Trading / Strategy Thread
          ↓
       SPSC Queue
          ↓
Core 3: Order Gateway / Execution Thread
```

核心思想是 **Single Writer + Message Passing**，尽量避免多个线程同时修改同一个 Order Book / Position，然后到处加 Mutex。

如果追求更低 latency，甚至可以把 Market Data → Strategy → Order Submission 放到一个 pinned core 上，减少 thread scheduling、lock 和 cache coherence 开销。

## 10. Low-Latency 优化

基本架构正确以后再谈性能优化。Hot Path 上尽量避免 `malloc/new`、Mutex、Logging、Database、System Call 和不必要的数据 Copy。Order / Message 可以预先分配 Memory Pool，Thread 固定到指定 CPU Core，通过 CPU Affinity 避免 scheduler 到处迁移线程；NIC 尽量和 Trading Core 放在同一个 NUMA Node。

```text
NIC
 │ PCIe
 ▼
CPU Socket
 ├── Core 2: Market Data
 ├── Core 3: Strategy
 └── Core 4: Order Gateway
```

继续追求 latency 时再考虑 Busy Polling、Huge Pages、`TCP_NODELAY`、Kernel Bypass（例如 Onload / DPDK）、Hardware Timestamping，最后才是 FPGA。不要一上来就 FPGA，应该先证明普通 software stack 不满足 latency requirement。

## 11. Reliability / Redundancy

如果真的只有“一根 Market Data Cable + 一根 Execution Cable”，两根都是 Single Point of Failure。Production System 最终应该考虑：

```text
Market Data A / B
Order Entry A / B
NIC A / B
Network Path A / B
Primary Server / Backup Server
```

Backup Server 可以同步 Order / Position State，但必须明确谁拥有 Order Entry 权限，否则 Primary 和 Backup 同时发送 Order 会造成 Duplicate Trading。常见方式是 Active-Passive + 明确的 Failover Ownership。

## 12. 面试时的 2 分钟版本

公司虽然在 Amsterdam，但我会把 latency-sensitive Trading Server 放到 Frankfurt Exchange 附近的 Colocation，通过 dedicated fiber cross-connect 连接 Exchange。Market Data 和 Order Entry 使用独立链路。

Market Data 如果 Exchange 支持，我会使用 UDP Multicast，因为它适合 one-to-many 的低延迟行情分发，同时通过 Sequence Number 检测 Packet Loss，出现 Gap 时通过 Snapshot / Recovery Channel 恢复。

Order Entry 则使用 Exchange 官方支持的可靠协议，例如 Persistent TCP Binary Protocol。每个 Order 有唯一 Order ID 和 Sequence Number，Exchange 返回 ACK、Reject 和 Fill，我们根据 Execution 更新 Order State 和 Position。

内部系统保持简单：`Market Data Gateway → Trading Engine → Risk Check → Order Gateway`。对外只有 `buy()` / `sell()`，内部统一成 `submitOrder(side, qty, price)`；Price 使用 Integer Tick 而不是 `double`。

Hot Path 上尽量避免 Lock、Allocation、Logging 和 Database，通过 Preallocated Memory、SPSC Queue、CPU Pinning、NUMA/NIC Affinity 降低 latency。更进一步才考虑 Busy Polling、Kernel Bypass 和 FPGA。Production 环境还需要 Redundant Network Path、Backup Server、Risk Limit 和 Kill Switch。
