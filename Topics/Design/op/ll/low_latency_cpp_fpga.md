# Low-Latency Trading System：C++、Market Data Receiver 与 FPGA

![Low-Latency Trading
Architecture](low_latency_trading_architecture.png)
![Low-Latency Trading
Architecture2](low_latency_trading_architecture2.png)

Trading Server? Colocation -> Trading Engine 尽可能靠近 Exchange
```
Amsterdam Office
    | private WAN / leased line
    v
Frankfurt Colo / Data Center
    |
    +---- Market Data Cable <----- Exchange
    |
    +---- Order Cable -----------> Exchange
```


```
Best latency / reliability
        ↑
Dedicated fiber
Private leased line
MPLS / Private WAN (运营商提供的企业专线) lower latency lower jitter more predictable routing better reliability / SLA
VPN over Internet
Public Internet
        ↓
Worst predictability
```

# Low-Latency Trading Service System Design

> 将 Trading System、Position Signal Queue、Hot Path/Data Model、SPMC
> Fan-out、C++/FPGA
> 等内容整合成一条完整的面试设计主线。重点不是堆技术名词，而是从
> **物理部署 → 网络 → Market Data → State/Event → Strategy → Risk →
> Order Entry → Queue/Fan-out → Recovery → C++/CPU → FPGA** 一层层推进。

------------------------------------------------------------------------

## 0. 先建立整张图

以"公司在 Amsterdam，交易 Frankfurt Exchange 的 BMW"为例：

``` text
                         Frankfurt Exchange
                    ┌──────────┴──────────┐
                    │                     │
             Market Data              Order Entry
             UDP/Multicast             TCP/Exchange Protocol
                    │                     ▲
                    ▼                     │
               NIC / RX NIC          NIC / TX NIC
                    │                     ▲
                    ▼                     │
          Market Data Receiver            │
          decode + sequence check          │
                    │                     │
                    ▼                     │
              Local Market State           │
              / Order Book                 │
                    │                     │
                    ▼                     │
                 Strategy                  │
                    │                     │
                    ▼                     │
              Pre-Trade Risk               │
                    │                     │
                    └──────→ Order Gateway ┘

             ┌──────────── Cold Path ────────────┐
             │ Logging / Metrics / GUI / DB      │
             │ Position snapshots / Analytics    │
             └───────────────────────────────────┘

Amsterdam Office
    │
    └── private WAN / leased line ──→ Frankfurt Colo
        monitoring / deployment / config / kill switch
```

**核心原则：真正决定交易 latency 的代码放在 Frankfurt colo。Amsterdam
office 不进入交易 hot path。**

Hot path 可以概括成：

``` text
Exchange packet
    ↓
NIC
    ↓
Market Data Decode
    ↓
OrderBook / Market State Update
    ↓
Strategy
    ↓
Risk
    ↓
Order Gateway
    ↓
NIC
    ↓
Exchange
```

收到 ACK / Reject / Fill 后：

``` text
Exchange
   ↓
Execution Report
   ↓
Order State / Position
   ├──→ Hot-path state update
   └──→ Cold-path logging / DB / GUI / monitoring
```

------------------------------------------------------------------------

# 1. 面试第一步：先问 Requirement

不要一上来就说 SPSC、FPGA、DPDK。

先问：

1.  我们是交易公司连接 Exchange，还是自己设计 Exchange / Matching
    Engine？
2.  latency target 是 ms、100us、10us 还是 sub-us？
3.  Market Data 是 UDP multicast 还是 TCP？
4.  Order Entry protocol 是 TCP 还是 exchange-specific protocol？
5.  每个 event 都必须处理吗，还是只关心 latest state？
6.  一个 producer / consumer，还是多个？
7.  consumer 是"竞争处理任务"，还是"每个人都要看到同一份数据"？
8.  queue 满了能不能 drop？
9.  crash 后允许丢多少数据？RPO 是多少？
10. 恢复要多快？RTO 是多少？
11. throughput 大概多少？burst 有多大？
12. 是否允许 dedicated CPU core / busy polling？
13. 是否需要跨 process / 跨 machine？

这些答案决定后面的设计。

------------------------------------------------------------------------

# 2. Physical Infrastructure：服务器到底放哪

## 2.1 Colocation

如果 Exchange 在 Frankfurt，而公司办公室在 Amsterdam：

``` text
Bad:
Amsterdam Trading Engine
       ↓ hundreds of km
Frankfurt Exchange

Better:
Amsterdam Office
       ↓ private WAN
Frankfurt Colo
       ↓ cross-connect
Frankfurt Exchange
```

Trading Engine 应该放在 Exchange 附近的 **colocation data center**。

原因非常简单：光传播也需要时间。软件优化 5us
没有意义，如果物理线路先多走几百公里。

## 2.2 Cross-connect

在 colo 中：

``` text
Exchange Network
      │
    Fiber
      │
Cross Connect
      │
    Our NIC
      │
Trading Server
```

这比绕公网 Internet 更低 latency、更低 jitter、路径也更可预测。

## 2.3 Amsterdam ↔ Frankfurt

办公室主要用于：

-   monitoring
-   GUI
-   deployment
-   configuration
-   historical analysis
-   risk dashboard
-   manual kill switch

连接方式从更可预测到更普通：

``` text
Dedicated fiber
Private leased line
MPLS / Private WAN
VPN over Internet
Public Internet
```

Office 不应该同步参与：

``` text
Market Data → Strategy → Risk → Order
```

否则 WAN latency 会直接进入 hot path。

------------------------------------------------------------------------

# 3. Network：两条 Exchange 线路分别干什么

通常可以先把系统理解成两条逻辑线路：

``` text
Exchange ── Market Data ──→ Trading System
Exchange ←── Orders ─────── Trading System
Exchange ── ACK/FILL ─────→ Trading System
```

服务器可以有不同 NIC / queue 来处理 RX 和 TX。

## 3.1 Market Data：为什么常见 UDP / Multicast

Market Data 的特点：

-   Exchange 一直往外推；
-   同一份行情有很多订阅者；
-   latency 比"每个 packet 必须靠 TCP 重传"更重要。

因此常见：

``` text
Exchange
   │ UDP Multicast
   ├──→ Firm A
   ├──→ Firm B
   └──→ Firm C
```

但是 UDP 可能丢包，所以 application protocol 通常需要：

``` text
sequence = 10001
sequence = 10002
sequence = 10003
sequence = 10005
```

发现 `10004` 不见了：

``` text
expected = 10004
received = 10005
→ GAP
```

此时不能继续假装本地 OrderBook 一定正确。

恢复可以是：

``` text
Gap detected
   ├──→ retransmission request
   ├──→ snapshot
   └──→ reconnect / recovery channel
```

所以：

> UDP 不等于"不可靠就不管了"。\
> 它只是把 reliability / recovery 的控制更多交给 application。

## 3.2 Order Entry：为什么通常要求可靠

订单不能随便丢：

``` text
Strategy
   ↓
BUY BMW 100
   ↓
Order Gateway
   ↓
Exchange
```

必须知道：

``` text
ACK?
REJECT?
PARTIAL FILL?
FULL FILL?
```

如果 Exchange 提供 TCP order-entry protocol，可以使用 persistent TCP
connection。

但 TCP 只保证：

``` text
ordered reliable byte stream
```

它不保证：

``` text
Exchange application 已经接受订单
Risk 已经通过
Order 已经进入 exchange state
```

所以仍然需要 application-level：

``` text
orderId
sequence number
ACK / Reject / Fill
session recovery
duplicate detection
```

例如连接在发送订单后断掉，不能直接无脑 resend，否则可能产生 duplicate
order。

------------------------------------------------------------------------

# 4. Market Data Receiver：packet 怎么变成 Strategy 能用的数据

Exchange 发来的不是：

``` cpp
MarketUpdate update;
```

而是 bytes：

``` text
Ethernet Header
IP Header
UDP Header
Exchange Protocol Message
```

Market Data Receiver 做：

``` text
NIC receives packet
      ↓
packet processing
      ↓
decode exchange protocol
      ↓
check sequence
      ↓
update local market state
      ↓
notify strategy
```

例如内部转换成：

``` cpp
struct MarketUpdate {
    int instrumentId;
    int price;
    int quantity;
    uint64_t sequence;
};
```

Hot path 上尽量避免：

``` cpp
std::string symbol;
std::map<std::string, double> fields;
```

更适合：

``` cpp
using InstrumentId = uint32_t;
using Price = int64_t;
using Quantity = int32_t;
```

因为 fixed-size numeric types 更容易：

-   contiguous storage
-   avoid allocation
-   avoid string comparison
-   improve cache locality
-   predictable latency

------------------------------------------------------------------------

# 5. Event vs State：整个系统最重要的区分之一

很多 queue / seqlock / snapshot 的选择，本质都来自：

> 这份数据是 **event**，还是 **state**？

## 5.1 Event

例如：

``` text
Trade 1: BUY +20
Trade 2: SELL -10
Trade 3: BUY +30
```

最终 position：

``` text
0 + 20 - 10 + 30 = 40
```

中间 event 不能随便丢。

适合：

``` text
Ring Buffer
Event Log
Sequence Number
Replay
```

## 5.2 State

例如：

``` text
AAPL price = 100
AAPL price = 101
AAPL price = 102
AAPL price = 105
```

某些 consumer 只关心：

``` text
latest AAPL price = 105
```

那么旧状态可以被覆盖/coalesce。

适合：

``` text
Latest-value table
Snapshot publication
Seqlock
Double / Triple Buffer
```

## 5.3 同一个 trading system 两者都会有

``` text
Market Data Event Stream
        ↓
OrderBook processing
        ↓
Current Market State
```

所以：

``` text
Event transport        → Ring Buffer
Current shared state   → Snapshot / Seqlock
```

不要问：

> "Seqlock 和 RingBuffer 哪个更好？"

它们通常解决的不是同一个问题。

------------------------------------------------------------------------

# 6. Seqlock：适合读 latest state，不适合保存 event history

例如 Strategy 只想读取一致的：

``` cpp
struct MarketState {
    int bid;
    int ask;
    int bidQty;
    int askQty;
};
```

Writer：

``` text
version = odd
write state
version = even
```

Reader：

``` text
v1 = version
if odd → retry

copy state

v2 = version

if v1 != v2 → retry
```

这样 reader 不需要 mutex。

优点：

-   reader 非常轻；
-   一个 writer 很适合；
-   read-mostly state 很好用。

缺点：

-   writer 太频繁时 reader 可能不断 retry；
-   reader starvation；
-   它没有 event history；
-   不能告诉你"中间发生过什么"。

因此：

``` text
Strategy needs latest BBO → seqlock/snapshot 可以
Risk needs every fill     → seqlock 不够
```

如果 writer 极其频繁，可以考虑：

``` text
double buffering
triple buffering
immutable snapshot publication
```

让 reader 读取一个稳定 snapshot，而 writer 在另一份 buffer 上更新。

------------------------------------------------------------------------

# 7. Queue：从最简单版本一路优化

## 7.1 Version 0：Blocking Queue

第一版可以先正确：

``` cpp
std::queue<T>
+ std::mutex
+ std::condition_variable
```

适合：

-   cold path
-   latency requirement 不极端
-   先保证 correctness

问题：

-   lock contention
-   sleep / wakeup
-   context switch
-   dynamic allocation
-   latency jitter

## 7.2 Bounded Queue

实际 queue 不能无限增长。

假设：

``` text
Producer = 2M events/sec
Consumer = 1M events/sec
```

queue 再大也只是延迟死亡。

所以 bounded queue 满了以后必须有 policy：

``` text
block / spin producer
drop
coalesce
spill
scale consumers
upstream rate control
```

关键还是 message semantics。

### Absolute state

``` text
AAPL=100
AAPL=110
AAPL=120
```

可能可以 coalesce 成：

``` text
AAPL=120
```

### Delta/event

``` text
+10
-5
+20
```

不能随便 drop。

------------------------------------------------------------------------

# 8. SPSC Ring Buffer：low-latency 最基础的 building block

如果：

``` text
Single Producer
Single Consumer
```

最自然：

``` text
Producer → [ Ring Buffer ] → Consumer
```

核心：

``` cpp
template <typename T, size_t N>
class SPSCQueue {
public:
    bool push(const T& value) {
        auto write = write_.load(std::memory_order_relaxed);
        auto next = (write + 1) % N;

        if (next == read_.load(std::memory_order_acquire))
            return false;

        buffer_[write] = value;

        write_.store(next, std::memory_order_release);
        return true;
    }

    bool pop(T& value) {
        auto read = read_.load(std::memory_order_relaxed);

        if (read == write_.load(std::memory_order_acquire))
            return false;

        value = buffer_[read];

        read_.store(
            (read + 1) % N,
            std::memory_order_release
        );
        return true;
    }

private:
    std::array<T, N> buffer_;

    alignas(64) std::atomic<size_t> write_{0};
    alignas(64) std::atomic<size_t> read_{0};
};
```

为什么快：

``` text
preallocated
no malloc/free
no mutex
contiguous memory
simple ownership
good cache locality
predictable operations
```

## 8.1 acquire / release 到底保护什么

Producer：

``` cpp
buffer_[write] = value;
write_.store(next, std::memory_order_release);
```

含义：

> payload 写完之后，才 publish 新 write index。

Consumer：

``` cpp
write_.load(std::memory_order_acquire);
```

看到新 index 后，也必须看到 index publish 之前的 payload writes。

反方向同理。

## 8.2 为什么 `alignas(64)`

Producer 不断写：

``` text
write_
```

Consumer 不断写：

``` text
read_
```

如果两个 atomic 在同一个 cache line：

``` text
Core A writes write_
Core B writes read_
       ↓
same cache line ownership bouncing
```

这叫 false sharing。

所以把 producer-owned 和 consumer-owned counters 分开 cache line。

------------------------------------------------------------------------

# 9. Multiple Producers：为什么 low-latency 常偏向 per-producer SPSC

如果：

``` text
Strategy A ─┐
Strategy B ─┼──→ Execution
Strategy C ─┘
```

可以做 MPSC。

但 shared write position 会产生：

``` text
CAS contention
atomic contention
cache line bouncing
retry
```

另一种常见 low-latency 设计：

``` text
Strategy A → SPSC A ─┐
Strategy B → SPSC B ─┼──→ Execution Thread
Strategy C → SPSC C ─┘
```

每个 producer 独占自己的 queue。

优点：

-   producer 之间不抢 write index；
-   ownership 清晰；
-   cache coherence 更简单；
-   latency 更 predictable。

缺点：

-   consumer 要 poll 多个 queue；
-   fairness 需要设计。

可以使用 bounded batching：

``` text
Q1 最多处理 N 个
→ Q2 最多 N 个
→ Q3 最多 N 个
→ repeat
```

避免一个 hot strategy 永远霸占 consumer。

------------------------------------------------------------------------

# 10. Multiple Consumers：Work Queue 和 Broadcast 完全不同

一定要先问：

> 每个 event 只需要一个 consumer 处理，还是每个 consumer 都要看到？

## 10.1 Work Queue

``` text
          ┌→ Worker A
Queue ────┤
          └→ Worker B
```

语义：

``` text
event → A OR B
```

这才是典型 MPMC worker queue。

## 10.2 Broadcast / Pub-Sub

Trading 中经常是：

``` text
Market Event
    ├──→ Strategy
    ├──→ Risk
    ├──→ Logger
    └──→ Monitoring
```

语义：

``` text
event → A AND B AND C AND D
```

这不是普通 work queue。

需要：

``` text
SPMC Broadcast
```

------------------------------------------------------------------------

# 11. SPMC V1：每个 consumer 都看 event stream

最直观的 broadcast ring：

``` text
Producer sequence = 100

Consumer A read = 99
Consumer B read = 97
Consumer C read = 80
```

如果 producer 不允许覆盖任何 consumer 没读的数据，它需要知道：

``` text
min(readA, readB, readC)
```

问题：

> Producer hot path 必须不断读取所有 consumer 的 progress。

Consumer 又不断修改这些 counters，因此会制造 cache-coherence traffic。

所以 low-latency 场景有另一种 philosophy：

> Producer 不等慢 consumer。Producer 一直写；慢 consumer 自己发现
> overflow，然后 recovery。

这让：

``` text
fast producer
≠ blocked by slow GUI/logger
```

------------------------------------------------------------------------

# 12. SPMC V1：Global Publish / Pending Index

V1 可以有：

``` cpp
struct Queue {
    alignas(64) std::atomic<uint64_t> publishedIndex;
    alignas(64) std::atomic<uint64_t> pendingIndex;
    // ring bytes...
};
```

概念：

``` text
pendingIndex
    = producer reserve / 正在写到哪里

publishedIndex
    = 已经完整写完，可以安全读取到哪里
```

写入流程：

``` text
Reserve
  ↓
pendingIndex advances
  ↓
write metadata + payload
  ↓
Publish
  ↓
publishedIndex advances
```

Consumer 只读取已经 publish 的区域。

优点：

-   producer 不追踪所有 reader；
-   reader 不修改 producer 的核心状态；
-   适合 single-producer broadcast。

问题是 global publish boundary 粒度比较粗，而且 variable-size records /
wrap-around / overwrite detection 会让 reader 逻辑变复杂。

------------------------------------------------------------------------

# 13. SPMC V2：Per-block Version

V2 可以把 ring 分成 blocks：

``` text
Block 0: [version][payload]
Block 1: [version][payload]
Block 2: [version][payload]
...
```

Producer 写某个 block：

``` text
mark version = writing
write payload
publish final version
```

Consumer：

``` text
read version before
copy payload
read version after

if changed / invalid
    retry or detect overwrite
```

这和 seqlock 有一点"版本校验"的相似性，但语义仍然不同：

``` text
Seqlock
→ protect a shared latest state

Versioned ring block
→ determine whether an event slot was overwritten while reading
```

Per-block version 的好处：

-   producer 不需要读取 consumer progress；
-   每个 consumer 可以自己判断是否落后；
-   slow consumer 不阻塞 producer；
-   producer hot path 更独立。

代价：

-   consumer 必须处理 overflow；
-   queue 不再保证无限期保存 event；
-   recovery protocol 必须明确。

------------------------------------------------------------------------

# 14. Slow Consumer：真正的问题不是"queue 用哪种 atomic"

假设：

``` text
Producer = 1.5M/sec
Consumer = 1.0M/sec
```

长期来看：

``` text
backlog += 0.5M/sec
```

任何 bounded queue 都会出问题。

解决思路不是：

``` text
把 1M slots 改成 10M slots
```

那只是多撑一会。

真正方案：

``` text
1. reduce consumer work
2. batch
3. shard
4. add consumers
5. coalesce state
6. drop noncritical data
7. recovery from snapshot
8. upstream rate control
```

对于 hot instrument，可以做 ownership/sharding：

``` text
Shard 0: AAPL
Shard 1: IBM + META + MSFT
Shard 2: NVDA + TSLA
```

重要原则：

> 同一个 instrument 尽量保持 single owner。

否则多个 thread 同时修改同一 instrument state，又会引入锁、ordering 和
coordination。

------------------------------------------------------------------------

# 15. Single Writer / Ownership

Trading system 中一个非常强的设计模式：

``` text
Instrument / Shard
      ↓
one owner thread
      ↓
all state mutation happens here
```

例如：

``` text
Shard 0 owns AAPL, IBM
Shard 1 owns MSFT, META
Shard 2 owns NVDA, TSLA
```

好处：

``` text
No mutex for local state mutation
Natural ordering
Better cache locality
Simple reasoning
```

这里"single writer"不代表整个程序只有一个线程。

而是：

> 某一份 mutable state 只有一个 thread 有权修改。

其他线程通过 message passing 把工作送给 owner。

------------------------------------------------------------------------

# 16. Hot Path vs Cold Path

Hot path 定义：

> 从收到 latency-sensitive input 到产生 latency-sensitive output
> 之间必须同步完成的路径。

Trading：

``` text
Market Data
   ↓
Book Update
   ↓
Strategy
   ↓
Risk
   ↓
Order
```

不要写成：

``` text
Market Data
   ↓
Book
   ↓
Disk Log
   ↓
Database
   ↓
GUI
   ↓
Strategy
   ↓
Order
```

更合理：

``` text
                  ┌──→ Logger
                  ├──→ Metrics
Market Data ──────┼──→ GUI
   │              └──→ DB / Analytics
   ▼
Strategy
   ↓
Risk
   ↓
Order
```

Cold path 慢一点通常没关系。

Hot path 重点是：

``` text
minimum work
no blocking I/O
no disk
no DB query
no unnecessary allocation
no unnecessary locks
```

------------------------------------------------------------------------

# 17. 为什么 Trading State 不应该每次查 Database

如果每次 Strategy decision 都：

``` text
Strategy
   ↓
SQL query
   ↓
Position DB
   ↓
Risk decision
```

latency 和 jitter 都不可接受。

Hot-path state 应该尽量：

``` text
in memory
owned locally
preloaded
incrementally updated
```

例如：

``` cpp
struct InstrumentState {
    Price bestBid;
    Price bestAsk;
    Quantity position;
    Quantity openBuyQty;
    Quantity openSellQty;
};
```

DB 主要负责：

``` text
durability
historical query
recovery
reporting
audit
cold-path state
```

所以不是"完全不要 database"，而是：

> Database 不应该成为每次交易 decision 的同步 dependency。

------------------------------------------------------------------------

# 18. FillEvent：什么时候更新 Position

这是很容易混淆的一点。

Strategy 决定：

``` text
BUY 100
```

并不代表 position 已经 +100。

需要区分：

``` text
Decision
Order Sent
ACK
Partial Fill
Fill
Reject
Cancel
```

例如：

``` text
Current Position = 0

Strategy sends BUY 100
→ position still 0
→ open buy quantity may become 100

Exchange ACK
→ position still 0

Exchange FILL 40
→ position = 40
→ remaining open quantity = 60

Exchange FILL 60
→ position = 100
```

所以 hot state 至少要区分：

``` text
position
open orders
pending exposure
```

Risk 不能只看已成交 position，否则大量 pending buy orders 可能突破
limit。

------------------------------------------------------------------------

# 19. FillEvent 为什么可能需要多条下游线路

收到 Fill 后：

``` text
Exchange
   ↓
FillEvent
   ├──→ Position/Risk state
   ├──→ Strategy
   ├──→ Persistence
   ├──→ Monitoring
   └──→ GUI
```

这里不能简单问：

> "应该用 SPSC 还是 SPMC？"

先看语义。

如果：

``` text
Fill handler → Position owner
```

一对一：

``` text
SPSC
```

如果同一个 Fill 必须被多个 consumer 看见：

``` text
SPMC broadcast
```

但工程上也可能拆成多个 SPSC：

``` text
Fill Handler
   ├──→ SPSC → Risk
   ├──→ SPSC → Logger
   └──→ SPSC → GUI publisher
```

这可以让 slow logger 不直接影响 risk consumer。

选择重点不是 queue 名字，而是：

``` text
ownership
delivery semantics
slow consumer policy
backpressure
```

------------------------------------------------------------------------

# 20. Busy Polling vs Blocking

Cold path：

``` cpp
condition_variable.wait(...)
```

很好，因为：

-   节省 CPU；
-   latency 不敏感。

Hot path dedicated core：

``` cpp
while (running) {
    if (queue.pop(msg)) {
        process(msg);
    }
}
```

busy polling 的优势：

``` text
no sleep
no wakeup
no context switch
lower jitter
```

代价：

``` text
burns CPU core
power
thermal pressure
```

所以常见：

``` text
Hot path → busy poll
Cold path → blocking/event-driven
```

------------------------------------------------------------------------

# 21. CPU Affinity / Core Pinning

如果说：

> Producer 和 consumer 各自 dedicated core。

还要知道如何保证：

``` text
CPU affinity
```

否则 OS scheduler 可能把 thread 从 Core 2 搬到 Core 7：

``` text
Core 2 cache hot
   ↓ migration
Core 7 cache cold
```

产生：

-   cache warm-up
-   scheduling jitter
-   unpredictable latency

Low-latency server 常把关键 threads pin 到固定 cores。

还会进一步考虑：

``` text
NUMA locality
NIC queue affinity
IRQ affinity
hyper-thread sibling interference
```

------------------------------------------------------------------------

# 22. Data Model：性能从结构设计开始

糟糕的 hot-path model：

``` cpp
struct Order {
    std::string symbol;
    std::string exchange;
    std::map<std::string, double> fields;
};

std::list<Order> orders;
```

可能到处出现：

``` text
allocation
pointer chasing
string compare
cache miss
copy
```

即使 profiler 没有一个"65% CPU 的坏函数"，系统仍然可以 everywhere slow。

更适合：

``` cpp
struct Order {
    uint64_t orderId;
    uint32_t instrumentId;
    int64_t price;
    int32_t quantity;
    Side side;
};
```

并尽量：

``` text
vector / array / preallocated pool
```

而不是大量 heap node。

------------------------------------------------------------------------

# 23. Size + Locality

CPU 真正访问 memory 时以 cache line 为单位。

所以：

``` text
small frequently-used objects
+
contiguous memory
+
stable access pattern
```

非常重要。

例如：

``` cpp
std::vector<Order>
```

通常比：

``` cpp
std::list<Order>
```

更 cache-friendly。

因为 vector：

``` text
[Order][Order][Order][Order]
```

list：

``` text
[Order] → pointer → [Order] → pointer → [Order]
```

后者容易 pointer chasing + cache miss。

对于 hot-path object，还可以把：

``` text
hot fields
```

和：

``` text
cold metadata
```

分开。

例如：

``` cpp
struct HotOrder {
    int price;
    int qty;
    int next;
};

struct ColdOrderMetadata {
    std::string clientName;
    std::string debugText;
};
```

Strategy / matcher 不需要碰 cold metadata。

------------------------------------------------------------------------

# 24. Memory Pool / Preallocation

Hot path 避免频繁：

``` cpp
new
delete
malloc
free
```

因为：

-   allocator latency；
-   lock/contention；
-   fragmentation；
-   cache unpredictability。

可以启动时预分配：

``` text
Order Pool
[0][1][2][3][4]...[N]
```

使用 integer index：

``` cpp
int orderIndex;
```

甚至 intrusive links：

``` cpp
struct Order {
    int id;
    int price;
    int qty;

    int next;
    int prev;
};
```

这样同价 FIFO queue 可以在 pool 内通过 index 串起来，不必每个订单创建
`std::list` node。

------------------------------------------------------------------------

# 25. Shared Memory：跨 Process 但还在同一台机器

如果：

``` text
Strategy Process
Execution Process
```

在同一 machine：

``` text
Shared Memory
```

可以让两个 process map 同一块 physical memory：

``` text
Strategy Process
      │
      ▼
Shared Memory Ring Buffer
      ▲
      │
Execution Process
```

相比 socket：

-   少一些 kernel/network overhead；
-   可以直接使用 ring-buffer/message-passing model。

但跨 machine 就不能靠 shared memory。

------------------------------------------------------------------------

# 26. 跨 Machine：TCP/UDP + Application Protocol

``` text
Machine A
Strategy
   ↓
Socket
   ↓
NIC
======== Network ========
NIC
   ↓
Socket
   ↓
Execution
Machine B
```

如果 event 必须可靠处理，通常需要：

``` text
sequence
message type
payload
application ACK
duplicate detection
recovery
```

即使使用 TCP，sequence 仍然有价值：

``` text
transport delivered bytes
≠
business operation processed
```

Application ACK 可以表示：

``` text
Execution processed through sequence 10500
```

------------------------------------------------------------------------

# 27. Ordering / Gap / Duplicate

不要用 timestamp 当唯一 ordering。

更可靠的是：

``` cpp
struct Event {
    uint64_t sequence;
    uint64_t timestamp;
    // payload
};
```

sequence 用于：

``` text
ordering
gap detection
duplicate detection
replay
recovery
```

例如：

``` text
100
101
103
```

立刻知道：

``` text
102 missing
```

而 timestamp 可能受到：

-   clock synchronization
-   same timestamp
-   network delay
-   scheduling delay

影响。

------------------------------------------------------------------------

# 28. Persistence：什么时候必须进入 Hot Path

如果 requirement：

> Every acknowledged event must survive machine crash.

那么你必须定义：

``` text
ACK boundary
```

### Strong durability

``` text
Event
  ↓
Durable Log
  ↓
ACK
```

优点：

``` text
RPO ≈ 0
```

代价：

``` text
storage latency enters critical path
```

### Lower latency

``` text
Event
   ├──→ Trading Consumer
   └──→ SPSC → Persistence Thread → batch append
```

优点：

``` text
disk I/O off hot path
```

代价：

``` text
machine crash may lose latest not-yet-durable events
```

这不是"技术上谁更高级"，而是业务 trade-off。

------------------------------------------------------------------------

# 29. RPO / RTO

## RPO --- Recovery Point Objective

问：

> crash 后最多允许丢多少最近的数据？

``` text
RPO = 0
→ acknowledged data cannot be lost

RPO = 100ms
→ latest ~100ms may be lost
```

## RTO --- Recovery Time Objective

问：

> crash 后多久必须恢复服务？

如果 log 有十亿条，restart 时从头 replay：

``` text
RTO 很差
```

所以常见：

``` text
Periodic Snapshot
      +
Incremental Log
```

恢复：

``` text
Load latest snapshot
      ↓
Replay events after snapshot
      ↓
Resume
```

------------------------------------------------------------------------

# 30. High Availability

如果 primary 挂了：

``` text
Primary
   ↓ replication
Standby
```

但是不能只说"再启动一个 consumer"。

尤其对于 SPSC：

``` text
Producer → SPSC → Consumer A
```

不能让：

``` text
Consumer A
Consumer B
```

同时随便竞争读取，否则 queue semantics 已经变了。

HA 需要明确：

``` text
active/passive
replicated log
checkpoint
ownership transfer
sequence boundary
duplicate prevention
```

------------------------------------------------------------------------

# 31. Dynamic Sharding / Ownership Migration

如果：

``` text
Shard 0 owns AAPL
```

现在要迁移到 Shard 1，不能直接：

``` cpp
mapping["AAPL"] = 1;
```

因为迁移瞬间可能：

``` text
old owner still processing seq 100
new owner starts seq 101
late seq 99 arrives
```

需要明确 handoff boundary，例如：

``` text
1. stop assigning new AAPL events to old owner
2. choose cutover sequence N
3. old owner drains through N
4. transfer snapshot/state
5. new owner starts from N+1
```

目标：

``` text
no loss
no duplicate
no reorder
single owner at any moment
```

------------------------------------------------------------------------

# 32. Kernel Networking：普通路径发生什么

普通 socket RX 大概：

``` text
NIC
 ↓
DMA packet into memory
 ↓
interrupt / polling
 ↓
kernel network stack
 ↓
socket buffer
 ↓
syscall
 ↓
user-space Market Data Receiver
```

这里可能有：

``` text
interrupt
context switch
kernel processing
buffering
copy
scheduler jitter
```

普通系统完全可以从 socket 开始。

只有 latency requirement 足够极端，再讨论 kernel bypass。

------------------------------------------------------------------------

# 33. Kernel Bypass / DPDK / Onload

Kernel bypass 的思想：

> 让 application 更直接地处理 NIC packet，减少传统 kernel networking
> path 的 overhead。

概念上：

``` text
Normal:
NIC → Kernel → Socket → Application

Bypass:
NIC → User-space packet processing → Application
```

可能使用：

``` text
DPDK
vendor user-space networking stack
Solarflare/Xilinx Onload
```

常见配套：

``` text
busy polling
dedicated core
huge pages
preallocated packet buffers
NIC queue affinity
```

不要一开始就说 DPDK。

面试 progression 更好：

``` text
socket
→ profiling shows networking overhead
→ busy polling / tuning
→ kernel bypass if required
```

------------------------------------------------------------------------

# 34. FPGA 在哪里

FPGA 可以理解成：

> 可编程硬件逻辑，不是"更快的 CPU thread"。

可能放在：

``` text
Exchange
   ↓
NIC / FPGA
   ↓
CPU Trading Engine
```

或者让某些逻辑直接在 FPGA：

``` text
packet parse
market-data filtering
book preprocessing
risk checks
order generation
```

优点：

``` text
very deterministic
parallel hardware pipeline
very low latency
```

缺点：

``` text
development complexity
harder iteration
limited flexibility
hardware expertise
deployment/debugging difficulty
```

所以并不是：

> "low latency = 全部 FPGA"。

常见 trade-off：

``` text
CPU
→ flexible, easy to change

FPGA
→ fastest/deterministic for stable narrow logic
```

Strategy 经常需要快速迭代，因此 C++ CPU 仍然非常重要。

------------------------------------------------------------------------

# 35. Strategy 为什么有时要 State，有时要 Events

假设 Strategy D 只想知道：

``` text
current best bid
current best ask
```

它不需要：

``` text
100 → 101 → 99 → 103
```

只要：

``` text
latest = 103
```

那么 state publication 非常适合。

但另一个 strategy 可能计算：

``` text
trade imbalance over last 100 events
```

它必须看到 event stream。

所以：

``` text
Strategy A → latest state
Strategy B → event stream
Strategy C → both
```

Trading architecture 不应该强迫所有 consumer 使用同一种 communication
primitive。

------------------------------------------------------------------------

# 36. GUI 为什么也是 State，但却不需要 Seqlock 极致优化

GUI 通常只关心：

``` text
latest position
latest PnL
latest price
latest health
```

它确实是 state。

但它在 cold path：

``` text
10ms / 100ms refresh
```

通常完全够。

所以：

``` text
State semantics
≠
必须使用 low-latency seqlock
```

你可以：

``` text
hot strategy state → seqlock/snapshot
GUI state → normal thread-safe snapshot / network update
```

Data semantics 和 latency requirement 是两个不同维度。

------------------------------------------------------------------------

# 37. 一份 Event 怎么同时去 Hot Path 和 Cold Path

例如 FillEvent：

``` text
                    ┌→ Risk / Position
Exchange Fill ──────┼→ Strategy
                    ├→ Logger
                    └→ DB
```

不要让：

``` text
DB latency
```

阻塞：

``` text
Risk state update
```

可以：

``` text
Fill Handler
   ↓
update critical local state
   ↓
publish lightweight events
   ├──→ SPSC logging queue
   ├──→ monitoring queue
   └──→ persistence queue
```

如果所有 consumer 必须读取同一个 event ring，也可以 broadcast；但如果
cold consumer 可能非常慢，拆独立 queue 往往更容易隔离 backpressure。

------------------------------------------------------------------------

# 38. End-to-End Hot Path 的一个合理 Thread Model

一个简单版本：

``` text
Core 0: Market Data RX
Core 1: Book + Strategy
Core 2: Order TX / Execution RX
Core 3: Risk / Position owner
Core 4: Persistence
Core 5: Monitoring / telemetry
```

但 thread 数不是固定答案。

更重要的是：

``` text
Who owns what state?
Where are the handoff points?
Which handoff needs every event?
Which handoff only needs latest state?
Which path is latency-sensitive?
```

极致 latency 下还可能：

``` text
Market Data + Book + Strategy + Risk + Order TX
```

尽量放在一个 hot thread，减少 thread handoff。

这说明：

> Queue 本身不是越多越好。

每增加一次 thread handoff，都可能增加：

``` text
cache coherence
atomic synchronization
queue latency
scheduling complexity
```

------------------------------------------------------------------------

# 39. "Stateless Trading Engine"要小心这个说法

Trading Engine 通常并不真正 stateless。

它往往维护：

``` text
OrderBook
Orders
Positions
Sequence Numbers
Risk State
Strategy State
```

更准确的说法是：

> Hot-path state 主要放在 process memory 中，而不是每次同步查询
> database。

它仍然是 stateful，只是 state 被 local ownership + memory optimized。

------------------------------------------------------------------------

# 40. 面试中推荐的完整 Progression

如果题目是：

> Design a low-latency trading system.

可以按这个顺序讲：

``` text
1. Clarify requirements
      ↓
2. Colo / physical placement
      ↓
3. Exchange connectivity
      ↓
4. Market Data: UDP/multicast + sequence
      ↓
5. Market Data Receiver
      ↓
6. Local OrderBook / Market State
      ↓
7. Strategy
      ↓
8. Pre-trade Risk
      ↓
9. Order Gateway / TCP / ACK-Fill
      ↓
10. Define hot path vs cold path
      ↓
11. Event vs State
      ↓
12. SPSC / SPMC / snapshot / seqlock
      ↓
13. Single writer + sharding
      ↓
14. Backpressure / slow consumers
      ↓
15. Memory layout / preallocation / cache
      ↓
16. CPU affinity / busy polling
      ↓
17. Persistence / RPO / RTO
      ↓
18. HA / recovery
      ↓
19. Kernel bypass
      ↓
20. FPGA only if latency requirement justifies it
```

这样 progression 会比一开始扔：

``` text
FPGA + DPDK + lock-free + Kafka
```

自然很多。

------------------------------------------------------------------------

# 41. 一张表记住 SPSC / SPMC / Seqlock / Snapshot

  -----------------------------------------------------------------------
  Requirement                         Better starting point
  ----------------------------------- -----------------------------------
  1 producer → 1 consumer，every      SPSC Ring Buffer
  event                               

  N producers → 1 consumer            MPSC or per-producer SPSC

  1 producer → N consumers，每人都看  SPMC Broadcast
  event                               

  N consumers 抢任务，每个 event      Work Queue / MPMC
  只处理一次                          

  Reader 只要 latest state            Seqlock / Snapshot

  Writer 很频繁，reader 不想一直      Double/Triple Buffer / Snapshot
  retry                               Publication

  Slow consumer 不能阻塞 producer     Overwrite-capable/versioned
                                      broadcast + recovery

  Every event must survive crash      Durable log / WAL

  Same machine, different processes   Shared-memory ring

  Different machines                  Network protocol + sequence +
                                      recovery
  -----------------------------------------------------------------------

------------------------------------------------------------------------

# 42. 最容易被追问的几个 Trade-off

## 42.1 为什么不用 mutex？

不是"mutex 永远慢"。

而是 hot path 可能要求：

``` text
lower tail latency
less contention
less scheduling jitter
```

如果只有 single writer，本来就可以通过 ownership 避免 lock。

## 42.2 为什么不用 Kafka？

Kafka 很适合 durable distributed event streaming，但不应该自动进入
microsecond trading hot path。

可以放在：

``` text
analytics
audit
historical pipelines
cold path
```

是否需要取决于 durability / throughput / operational requirements。

## 42.3 为什么不让 producer 等最慢 consumer？

如果 Strategy 需要 5us，而 GUI 卡了 2 秒：

``` text
GUI should not freeze trading
```

所以需要隔离 slow consumer。

## 42.4 为什么 sequence number 比 timestamp 重要？

sequence 表示明确逻辑顺序：

``` text
100 → 101 → 102
```

timestamp 主要表示时间，不天然保证唯一 total ordering。

## 42.5 queue 越大越好吗？

不是。

queue 大只能 absorb burst：

``` text
temporary producer > consumer
```

不能解决：

``` text
sustained producer > consumer
```

------------------------------------------------------------------------

# 43. 一个面试级 End-to-End 回答

如果 interviewer 让我从零设计，我会先把真正的 trading engine 放在
Frankfurt exchange 附近的 colocation facility，通过 cross-connect 连接
exchange，而 Amsterdam office 只负责
monitoring、deployment、configuration 和 GUI，不参与 hot path。

Market data side 我会根据 exchange 提供的 feed 使用 UDP multicast
或对应协议。Market Data Receiver 从 NIC 收 packet，decode binary
protocol，并通过 sequence number 做 ordering 和 gap
detection，然后更新本地 in-memory order book / market state。Strategy
直接读取这份本地 state 做 decision，避免同步 database lookup。

Strategy 产生 order 后先做 pre-trade risk，再交给 Order Gateway。Order
Entry 如果 exchange 提供 TCP，我会保持 persistent connection，但
application layer 仍然维护 order id、sequence、ACK/Reject/Fill
state，因为 TCP reliability 不等于 exchange business operation
已经完成。

线程间 communication 我会先看语义：如果一个 producer 到一个 consumer
且每个 event 都必须处理，用 preallocated SPSC ring buffer；如果一份
market event 要广播给多个 strategy，则使用 SPMC broadcast 或为不同
consumer 建独立 queue；如果 consumer 只需要 latest market
state，则不一定传所有 event，可以使用 seqlock 或 snapshot publication。

Hot path 上我会避免 disk、DB、GUI、dynamic allocation 和不必要的
lock。Logging、metrics、persistence 等通过独立 queue 送到 cold-path
threads。Data model 使用 compact fixed-size types、contiguous
storage、preallocated pools，并通过 single-writer ownership 减少
synchronization。

如果 profiling 证明普通 kernel networking 已经成为瓶颈，再考虑 busy
polling、CPU pinning、NIC affinity、kernel bypass；只有在 latency target
极端并且逻辑足够稳定时，才把 packet processing、risk 或部分 strategy
logic 下沉到 FPGA。

最后我会明确 recovery semantics：sequence number 做 gap
detection，snapshot + incremental log 做 restart；如果 acknowledged
event 必须 crash-safe，则 durable write 必须进入 acknowledgement
boundary，否则可以接受 non-zero RPO，把 persistence 异步移出 hot path。

------------------------------------------------------------------------

# 44. 最后记住一条主线

不要把 low-latency trading system 记成一堆独立名词：

``` text
SPSC
SPMC
seqlock
cache line
FPGA
DPDK
UDP
TCP
snapshot
```

它们其实都挂在同一条主线上：

``` text
Exchange
   ↓
How do bytes arrive quickly?
   ↓
Market Data Receiver
   ↓
How do I represent data efficiently?
   ↓
OrderBook / State
   ↓
Does consumer need EVENTS or latest STATE?
   ↓
Ring Buffer / Seqlock / Snapshot
   ↓
Strategy
   ↓
How do I avoid contention?
   ↓
Single Writer / Ownership / CPU Pinning
   ↓
Risk
   ↓
Order Gateway
   ↓
How do I send reliably?
   ↓
TCP / Sequence / Application ACK
   ↓
Exchange Fill
   ↓
How do I update state and fan out?
   ↓
SPSC / SPMC / Cold-path queues
   ↓
How do I survive failure?
   ↓
Snapshot / Log / RPO / RTO
```

**真正的核心不是"会多少 low-latency
名词"，而是能够解释每个技术为什么出现在这条路径的这个位置，以及它解决的具体问题。**

