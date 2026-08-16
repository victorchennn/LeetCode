# Low-Latency Trading System：核心交易路径

------------------------------------------------------------------------

## 0. 整体架构

``` text
Client
  ↓
Order Gateway
  ↓
Pre-Trade Risk
  ↓
Router / Symbol Partitioning
  ↓
Matcher + OrderBook
  ↓
Execution
  ├──→ Execution Report → Client
  └──→ Market Data Publisher → Subscribers
```

------------------------------------------------------------------------

## 1. Order Gateway

### 1.1 Gateway 是什么？

**Order Gateway = 交易系统接收外部订单的入口。**

假设一个 Trader 想下单：

``` text
BUY 100 AAPL @ $200
```

这笔订单不会直接进入 `OrderBook`，而是先经过 Gateway：

``` text
Client → Order Gateway → Risk Check → Matcher → OrderBook
```

Gateway 可以先简单理解成交易系统的"前台"。外部 Client
发来的所有订单，先由 Gateway 接收和处理，再交给内部交易系统。

------------------------------------------------------------------------

### 1.2 Gateway 主要做什么？

Gateway 最基本的职责可以概括成四步：

``` text
Receive → Parse → Validate → Forward
```

-   **Receive**：从网络接收 Client 发来的消息。
-   **Parse**：把网络中的 bytes / message 解析成系统内部的 `Order`。
-   **Validate**：检查订单格式和基本字段是否合法。
-   **Forward**：合法订单继续传给后面的 Risk Check / Matcher。

例如：

``` cpp
void onMessage(const NetworkMessage& msg) {
    Order order = parse(msg);

    if (!validate(order)) {
        reject(order);
        return;
    }

    sendToNextStage(order);
}
```

这里暂时不用关心 `NetworkMessage`、TCP、binary protocol
是怎么实现的，后面讲网络时再展开。

------------------------------------------------------------------------

### 1.3 Client 到底发送了什么？

从业务逻辑上看，Client 想表达的是：

``` text
BUY AAPL 100 @ 200
```

实际发送的数据可能包含：

``` text
clientId
orderId
symbol
side
price
quantity
```

Gateway 收到以后，可以转换成内部 C++ 对象：

``` cpp
enum class Side {
    BUY,
    SELL
};

struct Order {
    int clientId;
    int orderId;
    std::string symbol;
    Side side;
    int price;
    int quantity;
};
```

因此 Gateway 是外部网络世界和内部 C++ trading logic 之间的一层：

``` text
Network Message
      ↓
   Gateway
      ↓
 C++ Order Object
```

------------------------------------------------------------------------

### 1.4 Gateway 的 Validation 检查什么？

Gateway 主要检查**订单本身是否合法**，例如：

``` text
symbol 是否存在？
quantity 是否 > 0？
price 是否合法？
side 是否为 BUY / SELL？
orderId 格式是否正确？
```

例如 Client 发：

``` text
BUY AAPL -100 @ 200
```

`quantity = -100` 明显非法，所以：

``` text
Client
  ↓
Gateway
  ↓
Validation Failed
  ↓
Reject
```

这个订单根本不需要进入 Matcher。

------------------------------------------------------------------------

### 1.5 Gateway 和 Risk Check 有什么区别？

这是很容易混淆的一点。

Gateway 主要回答：

> **"这个订单本身是否合法？"**

例如：

``` text
BUY AAPL -100 @ 200
```

`quantity = -100`，订单格式/字段本身就有问题。

Risk Check 回答的是：

> **"这个客户是否被允许做这笔交易？"**

例如：

``` text
BUY 1,000,000 AAPL @ 200
```

这个订单本身完全合法：

``` text
symbol = AAPL        ✓
side = BUY           ✓
quantity = 1,000,000 ✓
price = 200          ✓
```

但是客户可能没有足够的资金，或者买完以后超过最大持仓限制。因此它能通过
Gateway validation，却可能被 Risk Check 拒绝。

整体关系：

``` text
Client
  ↓
Gateway
  │  订单本身合法吗？
  ↓
Risk Check
  │  客户允许做这笔交易吗？
  ↓
Matcher
```

------------------------------------------------------------------------

### 1.6 为什么不让 Client 直接访问 OrderBook？

如果设计成：

``` text
Client → OrderBook
```

那么 `OrderBook`
不仅需要负责撮合，还需要处理网络、消息解析、认证、validation、错误处理等事情。

这样职责混在一起：

``` text
OrderBook:
- network
- parsing
- validation
- authentication
- matching
- order management
```

更合理的设计是职责分离：

``` text
Gateway
    → 网络入口、解析、基本 validation

Risk Check
    → 风控

Matcher / OrderBook
    → 撮合交易
```

这样 Matcher 的 hot path 可以保持简单，也更容易优化 latency。

------------------------------------------------------------------------

### 1.7 面试需要记住什么？

最核心的一句话：

> **Order Gateway is the entry point of the trading system. It receives
> client orders, parses incoming messages, performs basic validation,
> and forwards valid orders to downstream components.**

可以简化记忆成：

``` text
Gateway = Trading System Entry Point

Receive
  ↓
Parse
  ↓
Validate
  ↓
Forward
```

下一步：

``` text
Client
  ↓
Gateway        ← 当前
  ↓
Risk Check     ← 下一步
  ↓
Router
  ↓
Matcher
  ↓
OrderBook
```

------------------------------------------------------------------------

## 2. Pre-Trade Risk Check

### 2.1 Risk Check 是什么？

订单通过 Gateway 以后，通常还不能直接进入 Matcher。

``` text
Client → Gateway → Risk Check → Router → Matcher → OrderBook
```

**Pre-Trade Risk Check =
在订单真正进入市场撮合之前，检查这个客户是否被允许做这笔交易。**

Gateway 主要检查"订单本身是否合法"，Risk Check
主要检查"客户能不能做这笔交易"。

------------------------------------------------------------------------

### 2.2 为什么需要 Risk Check？

假设 Client 发来：

``` text
BUY 1,000,000 AAPL @ $200
```

从 Gateway 的角度，这个订单没有问题：

``` text
symbol   = AAPL       ✓
side     = BUY        ✓
quantity = 1,000,000  ✓
price    = 200        ✓
```

但是这笔交易价值：

``` text
1,000,000 × $200 = $200,000,000
```

客户可能根本没有这么高的交易额度。

如果直接：

``` text
Gateway → Matcher
```

订单可能已经成交，之后再发现客户没有足够资金就太晚了。

所以必须：

``` text
Gateway
   ↓
Risk Check
   ↓
通过 → Matcher
失败 → Reject
```

这也是为什么叫 **Pre-Trade** Risk：检查发生在 trade 之前。

------------------------------------------------------------------------

### 2.3 Risk Check 通常检查什么？

最基础的几种检查是：

#### Position Limit

检查成交以后，客户持仓是否超过允许范围。

例如：

``` text
Current Position = 900 shares
Max Position     = 1000 shares

New Order:
BUY 200 AAPL
```

如果全部成交：

``` text
900 + 200 = 1100
```

超过 `1000`：

``` text
1100 > 1000 → Reject
```

简单代码：

``` cpp
if (currentPosition + order.quantity > maxPosition) {
    reject(order);
}
```

真实系统还需要正确处理 BUY / SELL，例如：

``` cpp
int newPosition = currentPosition;

if (order.side == Side::BUY)
    newPosition += order.quantity;
else
    newPosition -= order.quantity;

if (std::abs(newPosition) > maxPosition)
    reject(order);
```

------------------------------------------------------------------------

### 2.4 Credit Limit

Position Limit 关注"持仓多少"，Credit Limit
更接近"你最多能承担多大的交易金额 / 风险敞口"。

例如：

``` text
Credit Limit = $1,000,000

Current Usage = $800,000

New Order:
BUY 2,000 AAPL @ $200

Order Value = $400,000
```

那么：

``` text
$800,000 + $400,000
= $1,200,000
> $1,000,000

→ Reject
```

可以粗略理解成：

``` cpp
long long orderValue = 1LL * order.price * order.quantity;

if (creditUsed + orderValue > creditLimit) {
    reject(order);
}
```

实际交易系统的风险计算会复杂很多，但 system design
面试先理解这个模型就够了。

------------------------------------------------------------------------

### 2.5 Max Order Size

还可以直接限制单笔订单大小。

例如：

``` text
Max Order Size = 100,000 shares

BUY 2,000 AAPL
→ OK

BUY 5,000,000 AAPL
→ Reject
```

代码：

``` cpp
if (order.quantity > maxOrderSize) {
    reject(order);
}
```

这可以防止错误程序突然发送异常大的订单。

例如正常策略本来想发送：

``` text
BUY 1000 AAPL
```

结果程序 bug 发成：

``` text
BUY 100000000 AAPL
```

Max Order Size 可以在进入 Matcher 前直接挡住。

------------------------------------------------------------------------

### 2.6 Price Band

还可以检查价格是否明显异常。

假设 AAPL 当前市场价格大约：

``` text
$200
```

Client 突然发送：

``` text
BUY AAPL @ $20,000
```

格式本身没有错误：

``` text
price > 0 ✓
```

所以 Gateway validation 不一定拒绝。

但 Risk Check 可以认为价格偏离正常市场价格太多：

``` text
Reference Price = $200
Allowed Range   = ±10%

Valid Range:
$180 ~ $220
```

于是：

``` text
BUY @ $205 → OK
BUY @ $215 → OK
BUY @ $20,000 → Reject
```

这就是简单的 **Price Band Check**。

------------------------------------------------------------------------

### 2.7 Risk State 放在哪里？

现在出现一个非常重要的 low-latency 问题。

假设每个订单都这样：

``` text
Order
  ↓
Risk Check
  ↓
Database
  ↓
SELECT current_position ...
  ↓
SELECT credit_limit ...
  ↓
Result
```

如果系统要求处理几十万 orders/sec，这会非常慢。

因此 latency-sensitive 的 Risk Check 通常需要把关键状态放在内存中。

例如：

``` cpp
struct RiskState {
    int position;
    int maxPosition;

    long long creditUsed;
    long long creditLimit;

    int maxOrderSize;
};
```

然后：

``` cpp
std::unordered_map<int, RiskState> riskByClient;
```

收到订单：

``` cpp
RiskState& risk = riskByClient[order.clientId];
```

直接做几个内存读取和算术比较：

``` cpp
bool checkRisk(const Order& order, const RiskState& risk) {
    if (order.quantity > risk.maxOrderSize)
        return false;

    long long value =
        1LL * order.price * order.quantity;

    if (risk.creditUsed + value > risk.creditLimit)
        return false;

    return true;
}
```

核心思想：

``` text
Hot Path
不要每个 order 都访问慢速数据库

        ↓

需要频繁使用的 Risk State
尽量保存在 memory 中
```

------------------------------------------------------------------------

### 2.8 Risk Check 和 Gateway 再对比一次

这是最需要分清楚的地方：

  Component    主要问题                 示例
  ------------ ------------------------ ---------------------
  Gateway      订单本身合法吗？         quantity = -100
  Risk Check   客户允许做这笔交易吗？   position 超限
  Matcher      这个订单能和谁成交？     BUY 200 vs SELL 199

所以：

``` text
BUY AAPL -100 @ 200
        ↓
Gateway Reject

BUY 1,000,000 AAPL @ 200
        ↓
Gateway OK
        ↓
Risk Limit Exceeded
        ↓
Risk Reject

BUY 100 AAPL @ 200
        ↓
Gateway OK
        ↓
Risk OK
        ↓
Matcher
```

------------------------------------------------------------------------

### 2.9 面试需要记住什么？

一句话：

> **Pre-trade risk checks whether a valid order is allowed to enter the
> market based on limits such as position, credit, order size, and price
> bands.**

最简单记忆：

``` text
Gateway:
“这个订单合法吗？”

Risk:
“这个客户能做吗？”

Matcher:
“这个订单和谁成交？”
```

当前进度：

``` text
Client
  ↓
Gateway        ✓
  ↓
Risk Check     ✓
  ↓
Router         ← 下一步
  ↓
Matcher
  ↓
OrderBook
```

------------------------------------------------------------------------

## 3. Router / Symbol Partitioning

### 3.1 Router 是什么？

订单通过 Gateway 和 Risk Check 后，需要决定：

> **这个订单应该交给哪个 Matcher 处理？**

整体流程：

``` text
Client → Gateway → Risk Check → Router → Matcher → OrderBook
```

Router 本身通常不负责撮合。它只负责把订单送到正确的 Matcher。

例如系统有 4 个 Matcher：

``` text
                ┌→ Matcher 0
Order → Router ─┼→ Matcher 1
                ├→ Matcher 2
                └→ Matcher 3
```

Router 要做的就是决定：

``` text
AAPL order → Matcher ?
MSFT order → Matcher ?
TSLA order → Matcher ?
```

------------------------------------------------------------------------

### 3.2 为什么需要多个 Matcher？

最简单的系统完全可以只有一个 Matcher：

``` text
All Orders
    ↓
Matcher
    ↓
Order Books

AAPL Book
MSFT Book
TSLA Book
NVDA Book
...
```

这种设计非常简单，但如果订单量越来越大，一个 CPU core 可能处理不过来。

假设：

``` text
Incoming = 200,000 orders/sec

One Matcher capacity = 80,000 orders/sec
```

一个 Matcher 不够，因此需要多个：

``` text
                ┌→ Matcher 0
Orders → Router ├→ Matcher 1
                ├→ Matcher 2
                └→ Matcher 3
```

这就是把工作拆到多个 CPU core / Matcher 上。

------------------------------------------------------------------------

### 3.3 怎么拆？

一个非常自然的方法是按照 **symbol** 拆。

例如：

``` text
Matcher 0:
AAPL
GOOG

Matcher 1:
MSFT
AMD

Matcher 2:
TSLA
META

Matcher 3:
NVDA
INTC
```

这样：

``` text
BUY AAPL 100
      ↓
Router
      ↓
Matcher 0
      ↓
AAPL OrderBook
```

而：

``` text
SELL TSLA 200
      ↓
Router
      ↓
Matcher 2
      ↓
TSLA OrderBook
```

这种方式叫：

**Partitioning / Sharding**

也就是把数据和工作分成多个互相独立的部分。

------------------------------------------------------------------------

### 3.4 为什么同一个 Symbol 必须去同一个 Matcher？

这是这一部分最重要的概念。

假设：

``` text
Order A:
BUY AAPL 100 @ 200

Order B:
SELL AAPL 100 @ 199
```

这两个订单应该成交。

如果设计成：

``` text
Order A → Matcher 0 → AAPL Book A

Order B → Matcher 1 → AAPL Book B
```

两个 Matcher 各自只看到一部分 AAPL orders：

``` text
Matcher 0:
BUY 100 @ 200

Matcher 1:
SELL 100 @ 199
```

它们就无法简单地完成正确撮合。

所以应该保证：

``` text
ALL AAPL orders
       ↓
same Matcher
       ↓
same AAPL OrderBook
```

例如：

``` text
AAPL BUY  → Matcher 0
AAPL SELL → Matcher 0
AAPL BUY  → Matcher 0
AAPL SELL → Matcher 0
```

这样 Matcher 0 拥有完整的 AAPL OrderBook。

------------------------------------------------------------------------

### 3.5 最简单的 Router 怎么实现？

一种非常常见的简单方案：

``` cpp
size_t shardId =
    std::hash<std::string>{}(order.symbol) % numShards;
```

例如：

``` text
numShards = 4

hash("AAPL") % 4 = 0
hash("MSFT") % 4 = 2
hash("TSLA") % 4 = 1
```

于是：

``` text
AAPL → Matcher 0
MSFT → Matcher 2
TSLA → Matcher 1
```

关键不是具体 hash 出什么数字，而是：

``` text
相同 symbol
    ↓
相同 hash
    ↓
相同 shard
    ↓
相同 Matcher
```

所以所有 AAPL orders 都会进入同一个 Matcher。

------------------------------------------------------------------------

### 3.6 为什么这种设计对 Concurrency 很重要？

假设我们有：

``` text
Matcher 0 → CPU Core 0
Matcher 1 → CPU Core 1
Matcher 2 → CPU Core 2
Matcher 3 → CPU Core 3
```

它们可以同时工作：

``` text
Core 0: processing AAPL
Core 1: processing TSLA
Core 2: processing MSFT
Core 3: processing NVDA
```

因此系统获得了 parallelism。

但对于一个具体的 OrderBook，例如 AAPL：

``` text
只有 Matcher 0 修改 AAPL OrderBook
```

所以：

``` text
AAPL Book
   ↑
only Matcher 0
```

不需要：

``` cpp
std::mutex aaplMutex;
```

让多个线程抢着修改同一个 book。

这就是交易系统里非常重要的 **Single Writer** 思想。

------------------------------------------------------------------------

### 3.7 Single Writer 是什么？

**Single Writer = 一个状态只允许一个 thread 修改。**

例如：

``` text
AAPL OrderBook
      ↑
Matcher Thread 0 only
```

其他线程不能直接修改它。

因此：

``` text
Thread 0 → AAPL
Thread 1 → TSLA
Thread 2 → MSFT
Thread 3 → NVDA
```

每个 Matcher 可以独立运行。

好处：

-   减少 lock contention
-   更容易保证订单顺序
-   latency 更稳定
-   代码更简单
-   更容易 deterministic replay

------------------------------------------------------------------------

### 3.8 为什么不让多个 Thread 同时修改一个 OrderBook？

另一种看起来很自然的设计：

``` text
Thread 0 ─┐
Thread 1 ─┼→ AAPL OrderBook
Thread 2 ─┤
Thread 3 ─┘
```

然后：

``` cpp
std::mutex mutex;

void process(Order order) {
    std::lock_guard<std::mutex> lock(mutex);
    // modify order book
}
```

正确性可能可以通过 mutex 保证，但会带来两个问题。

#### 问题 1：Lock Contention

所有线程都想修改 AAPL：

``` text
Thread 0 → waiting
Thread 1 → owns mutex
Thread 2 → waiting
Thread 3 → waiting
```

既然最后实际上还是一次只能一个 thread 修改，那么开很多 writer thread
并没有真正让这个 OrderBook 并行。

#### 问题 2：Ordering

假设两个订单几乎同时到达：

``` text
Order A
Order B
```

分别被：

``` text
Thread 0 → Order A
Thread 1 → Order B
```

如果 Thread 1 先拿到 mutex：

``` text
B → processed first
A → processed second
```

这会让 ordering 更难控制。

Trading Matcher 非常在意：

``` text
A first
then B
then C
```

因此更自然的方案是：

``` text
A
B
C
↓
Single Queue
↓
Single Matcher Thread
↓
process A
process B
process C
```

------------------------------------------------------------------------

### 3.9 Router 和 Queue 的关系

Router 找到目标 Matcher 后，通常不是自己直接修改
OrderBook，而是把订单放入该 Matcher 的 input queue。

例如：

``` text
Gateway
   ↓
Risk
   ↓
Router
   ↓
Matcher 0 Input Queue
   ↓
Matcher 0 Thread
   ↓
AAPL OrderBook
```

多个 Matcher：

``` text
                    ┌→ Queue 0 → Matcher 0
Gateway → Router ───┼→ Queue 1 → Matcher 1
                    ├→ Queue 2 → Matcher 2
                    └→ Queue 3 → Matcher 3
```

Matcher 自己不断消费：

``` cpp
void matcherLoop() {
    while (running) {
        Order order = inputQueue.pop();
        process(order);
    }
}
```

这里先把 queue 理解成普通的 producer-consumer queue。

后面再单独讨论：

``` text
mutex queue
blocking queue
SPSC
MPSC
ring buffer
lock-free queue
backpressure
```

现在不用一次全部加入设计。

------------------------------------------------------------------------

### 3.10 如果 Hash 分配不均匀怎么办？

简单：

``` text
hash(symbol) % N
```

有一个问题：不同 symbol 的交易量差别可能非常大。

例如：

``` text
AAPL = 80,000 orders/sec
TSLA = 60,000 orders/sec
NVDA = 50,000 orders/sec

100 个小股票加起来
     = 10,000 orders/sec
```

如果碰巧：

``` text
Matcher 0:
AAPL
TSLA
NVDA
```

而：

``` text
Matcher 1:
很多低流量股票
```

就会变成：

``` text
Matcher 0 → overloaded
Matcher 1 → mostly idle
```

所以真正系统不一定简单使用 `hash % N`，也可以根据历史流量做 **load-aware
assignment**：

``` text
Matcher 0:
AAPL

Matcher 1:
TSLA

Matcher 2:
NVDA

Matcher 3:
many low-volume symbols
```

但是 system design 面试第一版完全可以先说：

> Start with hash-based symbol partitioning. If symbol traffic is highly
> skewed, we can move to load-aware static or dynamic assignment.

这是一个很自然的 follow-up improvement。

------------------------------------------------------------------------

### 3.11 如果一个 AAPL 就把一个 Core 打满怎么办？

这是更难的 follow-up。

假设：

``` text
Matcher 0 capacity:
100k orders/sec

AAPL:
150k orders/sec
```

不能简单说：

``` text
AAPL → Matcher 0 + Matcher 1
```

因为两个 Matcher 同时维护同一个 OrderBook，会重新出现 ordering 和
synchronization 问题。

第一步通常是：

``` text
AAPL
 ↓
Dedicated Matcher / Dedicated Core
```

然后优化这个单线程 hot path：

``` text
better data structures
preallocated memory
avoid locks
better cache locality
CPU affinity
faster networking
```

因此交易系统经常选择：

> **Scale across symbols, but keep one symbol's matching logic
> single-writer.**

------------------------------------------------------------------------

### 3.12 面试需要记住什么？

Router 最核心的作用：

> **Router sends each order to the Matcher responsible for that
> symbol.**

最重要的设计：

``` text
same symbol
    ↓
same matcher
    ↓
same order book
    ↓
single writer
```

这样可以同时获得：

``` text
Different symbols
→ parallel processing

Same symbol
→ deterministic sequential processing
```

整体架构现在变成：

``` text
Client
  ↓
Gateway
  ↓
Risk Check
  ↓
Router
  ├──→ Queue 0 → Matcher 0 → AAPL / GOOG
  ├──→ Queue 1 → Matcher 1 → TSLA / META
  ├──→ Queue 2 → Matcher 2 → MSFT / AMD
  └──→ Queue 3 → Matcher 3 → NVDA / INTC
```

当前进度：

``` text
Gateway       ✓
Risk Check    ✓
Router        ✓
Matcher       ← 下一步
OrderBook
Execution
Market Data
```

------------------------------------------------------------------------

## 4. Matcher + Order Book（简版）

### 4.1 Matcher 是什么？

**Matcher / Matching Engine = 根据撮合规则，把新订单和 Order Book
中已有的 opposite-side orders 进行成交。**

``` text
Router
  ↓
Matcher
  ↓
OrderBook
  ↓
Execution
```

核心规则是 **Price-Time Priority**：

``` text
BUY  → lowest ASK first
SELL → highest BID first

Same price → FIFO
```

例如：

``` text
ASKS:
200 → Order A: 50
200 → Order B: 80
201 → Order C: 100

New:
BUY 100 @ 201
```

撮合：

``` text
A: 50 @ 200
B: 50 @ 200

BUY fully filled
B remaining = 30
```

### 4.2 Matcher 和 Order Book 的区别

``` text
OrderBook = 状态 / 数据结构
Matcher   = 操作 OrderBook 的撮合逻辑
```

代码里经常直接合并在一个 `OrderBook` class 中，所以 system design
时理解职责即可。

### 4.3 Order Book 第一版数据结构

``` cpp
std::map<int, std::list<Order>, std::greater<int>> bids_;
std::map<int, std::list<Order>> asks_;
```

结构：

``` text
BIDS:
200 → A → B
199 → C

ASKS:
201 → D → E
202 → F
```

其中：

``` text
map
→ Price Priority

list / deque
→ same-price FIFO
```

为了支持快速 Cancel，再加：

``` cpp
struct OrderLocation {
    Side side;
    int price;
    std::list<Order>::iterator it;
};

std::unordered_map<int, OrderLocation> orderIndex_;
```

于是：

``` text
orderId
  ↓
orderIndex
  ↓
直接找到 Order
  ↓
erase
```

### 4.4 为什么不用 `priority_queue`？

OrderBook 不只是：

``` text
get best
pop best
```

还需要：

``` text
Cancel arbitrary order
Modify order
Maintain same-price FIFO
Inspect price levels
```

`priority_queue` 不擅长 arbitrary deletion，所以不适合作为完整
OrderBook。

### 4.5 Low-Latency 下怎么继续优化？

第一版：

``` text
map<Price, list<Order>>
+
unordered_map<OrderId, Location>
```

逻辑正确、面试容易解释，但 `map/list` 都有 pointer chasing
和动态内存分配问题。

如果 interviewer 继续追问 latency，再升级：

``` text
contiguous price levels
+
preallocated Order pool
+
integer index / handle
+
intrusive linked list
```

原则是：

``` text
先给简单正确版本
→ 找到性能瓶颈
→ 再针对 hot path 优化
```

### 4.6 Single Writer

同一个 symbol 的订单都由同一个 Matcher 顺序处理：

``` text
AAPL Orders
    ↓
one input queue
    ↓
one Matcher thread
    ↓
AAPL OrderBook
```

这样可以避免多个线程同时修改同一个 OrderBook：

``` text
less lock contention
+
clear ordering
+
more predictable latency
```

------------------------------------------------------------------------

## 5. Execution / Execution Report

### 5.1 Execution 是什么？

前面已经走到：

``` text
Client → Gateway → Risk → Router → Matcher → OrderBook
```

当 Matcher 发现两个订单可以成交时，就会产生一笔 **Execution（成交）**。

例如 OrderBook 里已有：

``` text
Order 1001:
SELL 50 AAPL @ 200
```

新订单：

``` text
Order 2001:
BUY 50 AAPL @ 200
```

两者满足成交条件：

``` text
bestAsk <= buyPrice
200 <= 200
```

于是 Matcher 产生：

``` text
Execution:
AAPL
50 shares
$200
```

可以先简单理解：

> **Order 是"我想交易"，Execution 是"实际上成交了"。**

------------------------------------------------------------------------

### 5.2 Order 和 Execution 不是一个东西

这是很重要的区别。

Order：

``` text
BUY 100 AAPL @ 200
```

表示：

> 我希望最多以 \$200 买 100 股。

但它不代表已经买到了 100 股。

例如当前 asks：

``` text
SELL 30 @ 199
SELL 40 @ 200
```

新订单：

``` text
BUY 100 @ 200
```

Matcher 会产生：

``` text
Execution 1:
30 @ 199

Execution 2:
40 @ 200
```

总共只成交：

``` text
70 shares
```

原订单剩余：

``` text
30 shares
```

剩余部分进入 OrderBook：

``` text
BUY 30 @ 200
```

所以：

``` text
1 Order
   ↓
0 / 1 / many Executions
```

------------------------------------------------------------------------

### 5.3 为什么一笔 Order 可以产生多笔 Execution？

因为新订单可能和多个 resting orders 成交。

例如：

``` text
ASKS:

Order A: SELL 30 @ 200
Order B: SELL 40 @ 200
Order C: SELL 50 @ 201
```

新订单：

``` text
Order X:
BUY 100 @ 201
```

按照 Price-Time Priority：

``` text
Execution 1:
X vs A
30 @ 200

Execution 2:
X vs B
40 @ 200

Execution 3:
X vs C
30 @ 201
```

因此：

``` text
Order X quantity = 100

30 + 40 + 30 = 100

→ Fully Filled
```

这三次成交应该是三个不同的 Execution。

------------------------------------------------------------------------

### 5.4 OrderId 和 ExecutionId 为什么不同？

每个 Order 通常有自己的：

``` text
OrderId
```

每次成交通常有自己的：

``` text
ExecutionId
```

例如：

``` text
OrderId = 5000
BUY 100 AAPL @ 201
```

它产生：

``` text
ExecutionId = 9001
30 @ 200

ExecutionId = 9002
40 @ 200

ExecutionId = 9003
30 @ 201
```

所以：

``` text
OrderId
→ identifies an order

ExecutionId
→ identifies a specific trade/fill
```

不能简单把两者当成一个 ID。

------------------------------------------------------------------------

### 5.5 Fill 是什么？

面试和 trading system 里经常听到：

``` text
fill
partial fill
fully filled
```

**Fill 基本可以理解成订单被成交。**

例如：

``` text
Order:
BUY 100
```

只成交：

``` text
30
```

叫：

``` text
Partial Fill
```

剩余：

``` text
70
```

如果之后再成交：

``` text
70
```

那么订单总共：

``` text
100 / 100
```

叫：

``` text
Fully Filled
```

所以常见状态：

``` text
NEW
PARTIALLY_FILLED
FILLED
CANCELLED
REJECTED
```

------------------------------------------------------------------------

### 5.6 Execution 对象里有什么？

可以先设计：

``` cpp
struct Execution {
    long long executionId;

    int buyOrderId;
    int sellOrderId;

    std::string symbol;

    int price;
    int quantity;
};
```

例如：

``` text
executionId = 9001
buyOrderId  = 5000
sellOrderId = 3000
symbol      = AAPL
price       = 200
quantity    = 30
```

意思是：

``` text
BUY Order 5000
和
SELL Order 3000

成交 30 shares @ 200
```

真实系统还会有很多字段，例如 timestamp、client、venue
等，但第一版不用一次加完。

------------------------------------------------------------------------

### 5.7 Execution Report 是什么？

**Execution 是系统内部发生的成交事件。**

Client 还需要知道自己的订单发生了什么。

所以系统需要向 Client 发送：

**Execution Report**

例如 Client 发：

``` text
Order 5000:
BUY 100 AAPL @ 201
```

系统可能先回复：

``` text
Order Accepted
OrderId = 5000
```

之后发生第一笔成交：

``` text
PARTIAL FILL

OrderId = 5000
ExecutionId = 9001
Filled = 30
Fill Price = 200
Remaining = 70
```

之后第二笔：

``` text
PARTIAL FILL

OrderId = 5000
ExecutionId = 9002
Filled = 40
Fill Price = 200
Remaining = 30
```

最后：

``` text
FILLED

OrderId = 5000
ExecutionId = 9003
Filled = 30
Fill Price = 201
Remaining = 0
```

所以 Execution Report 可以理解为：

> **交易系统告诉 Client：你的订单现在发生了什么。**

------------------------------------------------------------------------

### 5.8 Ack 和 Execution Report 有什么区别？

这两个也容易混淆。

Client 发：

``` text
BUY 100 AAPL @ 200
```

系统首先可能返回：

``` text
ACK / Accepted
```

意思是：

> 我已经接受你的订单。

这不等于：

> 你的订单已经成交。

例如：

``` text
Client
  │
  │ BUY 100 @ 200
  ▼
Gateway / Trading System
  │
  │ ACK: Accepted
  ▼
Matcher
```

如果当前：

``` text
Best Ask = 205
```

根本不能成交。

订单只是进入 OrderBook：

``` text
BUY 100 @ 200
```

之后过了 5 秒，一个新的 SELL 到来：

``` text
SELL 100 @ 200
```

这时才产生：

``` text
Execution Report:
FILLED 100 @ 200
```

因此：

``` text
ACK
= 系统接受订单

Execution Report
= 订单状态 / 成交结果
```

------------------------------------------------------------------------

### 5.9 Reject 也是一种响应

如果订单在 Gateway 或 Risk Check 被拒绝：

``` text
Client
  ↓
Gateway
  ↓
Invalid Quantity
```

系统需要告诉 Client：

``` text
REJECTED
Reason = Invalid Quantity
```

或者：

``` text
Client
  ↓
Risk Check
  ↓
Position Limit Exceeded
```

返回：

``` text
REJECTED
Reason = Position Limit Exceeded
```

所以 Client 可能收到的消息包括：

``` text
Accepted
Rejected
Partially Filled
Filled
Cancelled
```

------------------------------------------------------------------------

### 5.10 一个完整流程

假设 OrderBook：

``` text
ASKS:

Order A:
SELL 30 @ 200

Order B:
SELL 50 @ 201
```

Client 发送：

``` text
Order X:
BUY 100 @ 201
```

流程：

``` text
Client
  ↓
Gateway
  ↓
Risk
  ↓
Router
  ↓
Matcher
```

系统接受订单，可以发送：

``` text
ACK:
Order X Accepted
```

Matcher 开始撮合。

第一笔：

``` text
X vs A
30 @ 200
```

产生：

``` text
Execution 9001
quantity = 30
price = 200
```

Client 收到：

``` text
PARTIAL FILL
30 @ 200
Remaining = 70
```

第二笔：

``` text
X vs B
50 @ 201
```

产生：

``` text
Execution 9002
quantity = 50
price = 201
```

Client 收到：

``` text
PARTIAL FILL
50 @ 201
Remaining = 20
```

OrderBook 没有更多可以成交的 SELL。

所以剩余：

``` text
BUY 20 @ 201
```

进入 OrderBook。

最终：

``` text
Order X:

Original Quantity = 100
Filled Quantity   = 80
Remaining         = 20
Status            = PARTIALLY_FILLED
```

------------------------------------------------------------------------

### 5.11 成交价格到底用谁的价格？

这是一个常见问题。

假设 OrderBook 已经有：

``` text
SELL 100 @ 199
```

新订单：

``` text
BUY 100 @ 200
```

可以成交，因为：

``` text
199 <= 200
```

成交价格通常使用 **resting order（已经在 OrderBook 里的订单）的价格**。

所以这里：

``` text
Trade Price = 199
```

不是：

``` text
200
```

可以理解成：

``` text
SELL @ 199
已经在市场等待

新的 BUY 愿意最高出到 200

既然 199 就可以买到
→ 按 199 成交
```

所以之前 Matcher 代码：

``` cpp
execute(buy, sell, tradeQty, sell.price);
```

这里使用的是 resting sell order 的 price。

------------------------------------------------------------------------

### 5.12 Maker 和 Taker

这个概念也经常出现。

假设：

``` text
Order A:
SELL @ 200
```

先进入 OrderBook 等待。

它提供了 liquidity：

``` text
OrderBook:
SELL @ 200
```

所以 Order A 可以叫：

``` text
Maker
```

后来：

``` text
Order B:
BUY @ 200
```

一进来马上和 A 成交。

Order B 消耗了 OrderBook 中已有的 liquidity，所以叫：

``` text
Taker
```

简单记：

``` text
Maker
→ resting order
→ adds liquidity

Taker
→ incoming aggressive order
→ removes liquidity
```

这个概念后面讲交易费率或 market making
时会经常出现，但现在知道意思即可。

------------------------------------------------------------------------

### 5.13 为什么 Execution Report 需要可靠？

假设 Client 买：

``` text
BUY 100 AAPL
```

系统实际上已经成交：

``` text
100 shares
```

但是 Client 没收到 Execution Report。

Client 可能认为：

``` text
“我还没有买到。”
```

然后再次发送：

``` text
BUY 100 AAPL
```

结果实际持仓可能变成：

``` text
200 shares
```

这在交易系统里是严重问题。

因此 Order Entry / Execution Report 通常比普通 market-data update
更强调：

``` text
reliable delivery
ordering
duplicate handling
recovery
```

这也是后面为什么要讨论：

``` text
sequence number
idempotency
deduplication
replay
```

现在先记住：

> **成交结果不能随便丢。**

------------------------------------------------------------------------

### 5.14 如果 Execution Report 重复怎么办？

网络系统经常存在这种情况：

``` text
Server:
“我发了 Execution 9001，但不知道 Client 收到没有。”
```

为了可靠性，Server 可能重发：

``` text
Execution 9001
Execution 9001
```

Client 不能认为发生了两笔交易。

因为两个消息都有：

``` text
executionId = 9001
```

Client 可以：

``` text
第一次 9001 → process
第二次 9001 → duplicate → ignore
```

这就是后面会详细讲的：

``` text
unique ID
+
deduplication
+
idempotency
```

------------------------------------------------------------------------

### 5.15 Execution 和 Market Data 不一样

一次成交发生以后，至少有两类人需要知道。

第一类：

``` text
参与这笔交易的 Client
```

需要知道：

``` text
Your Order 5000
filled 30 @ 200
```

这是：

``` text
Execution Report
```

第二类：

``` text
所有订阅市场数据的人
```

可能需要知道：

``` text
AAPL traded 30 @ 200
```

或者 OrderBook 发生变化：

``` text
ASK 200 quantity changed
```

这是：

``` text
Market Data
```

因此：

``` text
                    ┌→ Execution Report → specific Client
Matcher → Execution ┤
                    └→ Market Data       → subscribers
```

Execution Report 是私人订单状态。

Market Data 是市场状态广播。

下一节会专门讲 Market Data。

------------------------------------------------------------------------

### 5.16 面试需要记住什么？

最重要的关系：

``` text
Order
= trading intention

Execution
= actual trade

Execution Report
= message telling the Client what happened
```

一笔 Order：

``` text
1 Order
   ↓
0 / 1 / many Executions
```

ID：

``` text
OrderId
→ identifies the order

ExecutionId
→ identifies one individual fill/trade
```

状态：

``` text
NEW
PARTIALLY_FILLED
FILLED
CANCELLED
REJECTED
```

系统现在：

``` text
Client
  ↓
Gateway             ✓
  ↓
Risk Check          ✓
  ↓
Router              ✓
  ↓
Matcher             ✓
  ↓
OrderBook           ✓
  ↓
Execution           ✓
  ├──→ Execution Report → Client
  │
  └──→ Market Data       ← 下一步
```

下一步讲 **Market Data**：为什么交易系统需要另外一条 market-data
path、snapshot 和 incremental update 是什么，以及为什么 market data 常见
UDP multicast，而订单入口通常不能简单这么做。

------------------------------------------------------------------------

## 6. Market Data

### 6.1 Market Data 是什么？

前面已经完成了订单交易主路径：

``` text
Client
  ↓
Gateway
  ↓
Risk
  ↓
Router
  ↓
Matcher
  ↓
OrderBook
  ↓
Execution
```

但是除了下单的 Client，市场里的其他参与者也需要知道：

``` text
AAPL 当前 Best Bid 是多少？
Best Ask 是多少？
每个价格有多少 quantity？
刚才有没有成交？
成交价格是多少？
```

这些信息统称为：

**Market Data（市场数据）**

例如：

``` text
AAPL

Best Bid: $200 x 500
Best Ask: $201 x 300

Last Trade:
$200.50 x 100
```

------------------------------------------------------------------------

### 6.2 Execution Report 和 Market Data 的区别

这是最重要的区别。

假设：

``` text
Client A:
BUY 100 AAPL @ 200

Client B:
SELL 100 AAPL @ 200
```

成交：

``` text
100 @ 200
```

Client A 需要收到：

``` text
Your BUY order 123
FILLED 100 @ 200
```

Client B 需要收到：

``` text
Your SELL order 456
FILLED 100 @ 200
```

这是：

``` text
Execution Report
```

但市场上可能还有：

``` text
Client C
Client D
Client E
Trading Firm F
Trading Firm G
```

他们没有参与这笔订单，但也可能想知道：

``` text
AAPL just traded:
100 @ 200
```

这是：

``` text
Market Data
```

所以：

``` text
Execution Report
→ private
→ sent to the client whose order was affected

Market Data
→ public/shared market information
→ sent to market-data subscribers
```

------------------------------------------------------------------------

### 6.3 Market Data 包含什么？

常见市场数据包括：

``` text
Best Bid / Best Ask
Order Book changes
Trades
Volume
Price levels
```

例如 OrderBook：

``` text
BIDS                  ASKS

200 x 500             201 x 300
199 x 800             202 x 600
198 x 400             203 x 900
```

这就是一种 market data。

如果只发送：

``` text
Best Bid = 200 x 500
Best Ask = 201 x 300
```

通常叫：

``` text
Top of Book
```

如果发送多个 price levels：

``` text
200 x 500
199 x 800
198 x 400

201 x 300
202 x 600
203 x 900
```

通常叫：

``` text
Depth of Book / Market Depth
```

------------------------------------------------------------------------

### 6.4 Market Data 从哪里来？

我们自己的交易系统里，Matcher 每次修改 OrderBook，都知道发生了什么。

例如：

``` text
Before:

ASK 201 → 300 shares
```

发生：

``` text
BUY matches 100 shares @ 201
```

After：

``` text
ASK 201 → 200 shares
```

Matcher 可以产生一个 market-data event：

``` text
AAPL
ASK
price = 201
newQuantity = 200
```

然后：

``` text
Matcher
   ↓
Market Data Publisher
   ↓
Subscribers
```

完整一点：

``` text
                  ┌→ Execution Report → Client
Matcher / Book ───┤
                  └→ Market Data Publisher → All Subscribers
```

------------------------------------------------------------------------

### 6.5 为什么不能每次都发送整个 OrderBook？

假设 AAPL OrderBook 有：

``` text
10,000 price levels
```

每发生一笔小变化：

``` text
ASK 201:
300 → 290
```

如果系统都重新发送：

``` text
整个 10,000-level OrderBook
```

非常浪费 bandwidth。

因此 Market Data 通常使用：

``` text
Snapshot
+
Incremental Updates
```

这是这一部分最重要的概念之一。

------------------------------------------------------------------------

### 6.6 Snapshot 是什么？

**Snapshot = 某一个时间点的完整市场状态。**

例如：

``` text
AAPL Snapshot

Sequence = 1000

BIDS:
200 x 500
199 x 800
198 x 400

ASKS:
201 x 300
202 x 600
203 x 900
```

Client 收到以后，可以在自己内存里建立：

``` text
Local AAPL OrderBook
```

也就是说 Client 自己保存一份 market-data view。

------------------------------------------------------------------------

### 6.7 Incremental Update 是什么？

Snapshot 之后，不需要一直发送完整 OrderBook。

只发送：

> **发生了什么变化。**

例如：

``` text
Sequence 1001:
ASK 201 quantity:
300 → 250
```

Client 本地原来：

``` text
ASK 201 = 300
```

应用 update：

``` text
300 → 250
```

再来：

``` text
Sequence 1002:
ADD BID 200 quantity +100
```

Client：

``` text
BID 200:
500 → 600
```

再来：

``` text
Sequence 1003:
REMOVE ASK 202
```

Client 删除：

``` text
ASK 202
```

所以：

``` text
Snapshot
   ↓
建立完整状态

Incremental Update
   ↓
不断修改本地状态
```

------------------------------------------------------------------------

### 6.8 为什么需要 Sequence Number？

假设 Client 收到：

``` text
1001
1002
1003
1004
```

说明 update 顺序完整。

但如果收到：

``` text
1001
1002
1004
```

Client 马上知道：

``` text
1003 missing
```

这叫：

``` text
Sequence Gap
```

如果没有 sequence number：

``` text
Update A
Update B
Update D
```

Client 甚至不知道自己漏掉了 C。

所以每条 market-data message 通常带：

``` cpp
uint64_t sequence;
```

例如：

``` cpp
struct MarketUpdate {
    uint64_t sequence;

    std::string symbol;
    Side side;

    int price;
    int quantity;
};
```

------------------------------------------------------------------------

### 6.9 丢了一条 Market Data 怎么办？

假设 Snapshot：

``` text
seq = 1000

ASK 201 = 300
```

然后：

``` text
1001:
ASK 201 = 250

1002:
ASK 201 = 200

1003:
ASK 201 = 150
```

Client 实际只收到：

``` text
1001
1003
```

那么它发现：

``` text
expected = 1002
received = 1003
```

说明：

``` text
gap detected
```

这时候不能假装什么都没发生，否则 Client 的 local OrderBook
可能已经错了。

需要：

``` text
Detect Gap
   ↓
Recovery
   ↓
Get missing updates
OR
Get a fresh Snapshot
   ↓
Rebuild local state
   ↓
Continue incremental updates
```

最简单的恢复策略就是：

``` text
“我丢数据了，重新拿一个完整 Snapshot。”
```

------------------------------------------------------------------------

### 6.10 为什么 Market Data 很适合 One-to-Many？

Execution Report：

``` text
Order 123 filled
```

只需要发给拥有 Order 123 的 Client。

但 Market Data：

``` text
AAPL Best Bid changed
```

可能需要同时告诉：

``` text
100 clients
1,000 clients
10,000 clients
```

所以它天然是：

``` text
One Producer
      ↓
Many Consumers
```

例如：

``` text
                  ┌→ Trading Firm A
                  ├→ Trading Firm B
Exchange Market ──┼→ Trading Firm C
Data              ├→ Trading Firm D
                  └→ Trading Firm E
```

这就是为什么后面会碰到：

``` text
Multicast
```

------------------------------------------------------------------------

### 6.11 Unicast 是什么？

先理解最简单的网络发送方式。

如果 Server 要分别发给三个人：

``` text
Server → Client A
Server → Client B
Server → Client C
```

Server 实际发送三份：

``` text
same data × 3
```

这叫：

``` text
Unicast
```

也就是：

``` text
one sender → one receiver
```

如果有 10,000 个 Client：

``` text
Server
  ├→ Client 1
  ├→ Client 2
  ├→ Client 3
  ...
  └→ Client 10000
```

Server 可能需要发送 10,000 份相同 market data。

------------------------------------------------------------------------

### 6.12 Multicast 是什么？

Multicast 可以先粗略理解成：

> **发送方把一份数据发送到一个 multicast group，订阅这个 group 的多个
> receiver 都可以收到。**

逻辑上：

``` text
                    ┌→ Client A
Market Data → Group ├→ Client B
                    ├→ Client C
                    └→ Client D
```

发送方不需要为每个 subscriber 单独生成一份相同的数据流。

这很适合：

``` text
Exchange Market Data
```

因为：

``` text
同样的 market update
需要被很多 trading firms 收到
```

------------------------------------------------------------------------

### 6.13 为什么经常听到 UDP Multicast？

Market Data 系统经常使用：

``` text
UDP Multicast
```

这里先不要深入 UDP 的所有细节。

现在只记两个特点：

``` text
UDP:
fast / lightweight
但不保证可靠送达

Multicast:
适合 one-to-many distribution
```

组合：

``` text
UDP Multicast
```

非常适合：

``` text
大量 market-data updates
      ↓
快速广播给很多 subscribers
```

但代价就是：

``` text
packet 可能丢
```

因此才需要前面讲的：

``` text
Sequence Number
+
Gap Detection
+
Recovery
```

这些概念其实是连在一起的。

------------------------------------------------------------------------

### 6.14 为什么 Order Entry 不直接照搬 UDP Multicast？

Order Entry 是：

``` text
Client → Exchange
```

例如：

``` text
BUY 100 AAPL
```

这个消息非常重要。

如果直接丢了：

``` text
Client:
“I sent BUY 100.”

Exchange:
“I never received it.”
```

会产生严重的不确定性。

而且订单本身通常是：

``` text
one client → exchange
```

不是：

``` text
one sender → thousands of subscribers
```

所以 Order Entry 和 Market Data 的通信需求不同。

可以先粗略记：

``` text
Order Entry:
reliability 非常重要

Market Data:
latency + one-to-many throughput 非常重要
允许通过 sequence/recovery 处理 packet loss
```

后面单独讲 TCP / UDP 时再展开。

------------------------------------------------------------------------

### 6.15 外部 Market Data Feed 是什么？

到目前为止我们讲的是：

> **我们自己的交易系统产生自己的 Market Data。**

但题目还可能说：

``` text
ingest market data from multiple venues
```

意思是我们的系统还需要接收别人的 market data。

例如：

``` text
NYSE ─────┐
NASDAQ ───┼→ Our Trading System
ARCA ─────┘
```

每个 exchange / venue 都可能发送：

``` text
AAPL Bid/Ask
Trades
Book Updates
```

我们的系统需要接收这些数据。

这通常由：

``` text
Feed Handler
```

负责。

------------------------------------------------------------------------

### 6.16 Feed Handler 是什么？

**Feed Handler = 专门接收并解析某个外部 market-data feed 的组件。**

例如：

``` text
NASDAQ Feed
     ↓
NASDAQ Feed Handler
     ↓
Internal Market Update
```

另一个：

``` text
NYSE Feed
     ↓
NYSE Feed Handler
     ↓
Internal Market Update
```

为什么需要这一层？

因为不同 venue 的消息格式可能不同：

``` text
NASDAQ:
[Symbol][Px][Qty][Side]...

NYSE:
[InstrumentId][Price][Size][Type]...
```

内部系统不希望所有策略都分别理解这些格式。

所以：

``` text
External Format
      ↓
Feed Handler
      ↓
Normalized Internal Format
```

------------------------------------------------------------------------

### 6.17 Normalize 是什么？

假设两个 exchange：

``` text
Exchange A:

symbol = "AAPL"
price = 20000   // cents
qty = 100
```

Exchange B：

``` text
instrumentId = 12345
price = 200.00
size = 100
```

虽然表达方式不同，本质都是：

``` text
AAPL
$200
100 shares
```

Feed Handler 可以统一转换成：

``` cpp
struct MarketUpdate {
    int instrumentId;
    int price;
    int quantity;
    Side side;
};
```

于是后面的 trading logic 不需要关心：

``` text
这个数据来自 NASDAQ？
NYSE？
ARCA？
```

只处理统一的内部格式。

这叫：

``` text
Normalization
```

------------------------------------------------------------------------

### 6.18 Market Data 的完整路径

现在可以把两个方向分开。

#### 我们接收外部市场数据

``` text
Exchange A ─┐
Exchange B ─┼→ Feed Handlers → Normalize → Internal Market Data
Exchange C ─┘
```

#### 我们自己的 Matcher 产生市场数据

``` text
Client Order
    ↓
Matcher
    ↓
OrderBook Changes / Trades
    ↓
Market Data Publisher
    ↓
Subscribers
```

这是两个不同方向，不要混在一起。

------------------------------------------------------------------------

### 6.19 为什么 Market Data 也属于 Hot Path？

假设系统：

``` text
200,000 orders/sec
```

每个 order 可能产生：

``` text
0~多个 book updates
0~多个 trades
```

Market Data Publisher 可能每秒需要处理几十万甚至更多 messages。

如果每次都：

``` text
allocate memory
serialize huge object
lock global mutex
copy many times
```

latency 会受到影响。

所以真正 low-latency system 后面还会优化：

``` text
binary encoding
preallocated buffers
batching
zero-copy
lock-free / SPSC queues
CPU affinity
UDP multicast
```

但还是同样原则：

``` text
先理解正确的数据流
再做性能优化
```

不要一开始就把所有优化术语堆进去。

------------------------------------------------------------------------

### 6.20 面试需要记住什么？

最核心区别：

``` text
Execution Report
→ 告诉某个 Client：
  “你的订单发生了什么”

Market Data
→ 告诉所有订阅者：
  “市场发生了什么”
```

Market Data distribution：

``` text
Snapshot
   +
Incremental Updates
   +
Sequence Numbers
   +
Gap Detection
   +
Recovery
```

为什么 sequence number 重要：

``` text
1001
1002
1004
 ↑
1003 missing
```

为什么 UDP multicast 常见：

``` text
Market Data
→ high volume
→ low latency
→ one-to-many
```

而 packet loss 可以通过：

``` text
sequence + recovery
```

检测和恢复。

系统现在：

``` text
Client
  ↓
Gateway                 ✓
  ↓
Risk Check              ✓
  ↓
Router                  ✓
  ↓
Matcher                 ✓
  ↓
OrderBook               ✓
  ↓
Execution               ✓
  ├→ Execution Report   ✓
  └→ Market Data        ✓
```

到这里，**正常情况下的一笔订单怎么进入、撮合、成交、反馈和广播**已经完整了。

下一步开始进入这道 Low-Latency Trading System 题真正重要的可靠性部分：

``` text
如果 Matcher 突然 crash 怎么办？
```

这会引出：

``` text
Persistence
Append-Only Log
Snapshot
Replay
RPO
RTO
Deterministic Recovery
```

下一节先只从最基础的 **Persistence：为什么不能只把 OrderBook
放在内存里** 开始。

------------------------------------------------------------------------

## 7. 当前应该形成的整体理解

``` text
Gateway
→ 接收、解析、基础 validation

Risk Check
→ 判断客户是否允许做这笔交易

Router
→ same symbol → same Matcher

Matcher + OrderBook
→ single writer + price-time priority

Execution Report
→ 告诉具体 Client 订单发生了什么

Market Data
→ 告诉所有 subscribers 市场发生了什么
```

到这里是**正常交易路径**。下一阶段再进入可靠性：

``` text
Persistence
Append-Only Log
Snapshot / Replay
Crash Recovery
RPO / RTO
Idempotency
Backpressure
Latency Optimization
```
