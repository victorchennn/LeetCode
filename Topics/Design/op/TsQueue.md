# Designing a Trading Position Signal Queue


- Do we need to process every signal, or do we only care about the latest position for each instrument? event 还是 state
- How many producers and consumers do we have? Is this SPSC, MPSC, or MPMC?
- What are the expected throughput and latency requirements?

Since we already established that every signal must be processed, I cannot drop or overwrite signals. If the bounded queue is full, I need some form of backpressure. 
Producer -> queue full -> temporarily wait / retry

极度在意latency而且 producer/consumer 都绑定独立 CPU core，我可能让 producer busy-wait 一小段时间，直到 consumer 腾出 slot. 这样没有 sleep/wakeup 和 context switch，latency 更可预测。但这只适合 短暂 overload。如果 consumer 长期比 producer 慢，busy-wait 只是把问题藏起来，queue 最终还是永远满着。所以我会继续追问系统层面：

Is temporary backpressure acceptable? Can the producer slow down? If not, do we need a larger buffer, batching, multiple consumers, or an upstream rate-control mechanism?

A bounded queue protects the system from unbounded memory growth. When it fills, that is a signal that downstream capacity is insufficient. For transient overload I can apply backpressure or spin briefly, but for sustained overload I need to fix capacity or change the system design.

When would you choose busy polling, and when would you choose blocking? 对于 microsecond-level low latency 的 hot path，如果 producer 和 consumer 都有 dedicated core，我会更倾向于 busy polling。因为 consumer 不进入 sleep，不需要 condition variable 唤醒，也不会发生额外的 context switch，所以 latency 和 jitter 更低、更可预测。 代价就是它会一直占一个 CPU core，即使没有数据也在跑。
Hot path Strategy → Signal Queue → Execution = busy polling
Cold path Logging / Metrics / GUI = blocking

You said you would dedicate a CPU core to the producer and consumer. How would you actually make sure they stay on those cores? I would pin the producer and consumer threads to dedicated CPU cores using CPU affinity, so the OS scheduler doesn't move them between cores.

Your SPSC queue is entirely in memory. What happens if the trading process or machine crashes? Do we need to persist every position signal? How would you design crash recovery?
Do we need to recover every historical signal after a crash, or can we reconstruct the current state from another authoritative source?
情况 A：这些 signal 必须一个不漏地重新处理 durable recording、log / low-latency trade-off 如果要求 durable before accepted，持久化就可能进入 hot path，增加 latency。
情况 B：signal 本身不用恢复，只需要恢复正确的 current position  Position database / snapshot

Every signal represents an important trading decision and must be recoverable. We cannot lose an acknowledged signal. How would you add durability without destroying the latency of the hot path? append-only log
                     ┌──→ SPSC Queue → Trading Consumer
Producer → Signal ───┤
                     └──→ Logging Queue → Persistence Thread
                                         ↓
                                      Append Log
所以这里存在一个不可消除的 trade-off：Strong durability - Low latency
RPO = Recovery Point Objective？0 意味着：已经确认接受的数据，一条都不能丢。那么 ACK 前就必须有足够强的 durability guarantee。
RPO = 100 ms 意味着 crash 后允许最多损失最近大约 100ms 尚未 durable 的数据。通过 batching 减少 persistence overhead。

RTO = Recovery Time Objective crash 后多久必须恢复服务？5s? Primary - Standby - read log / replay - reconstruct state - resume
RTO < 100 ms snapshot + incremental log

If every acknowledged signal must survive a crash, I first need to define the acknowledgement boundary. The safest design is to append the signal to a durable log before acknowledging it, but that puts persistence on the latency-sensitive path.
If the business can tolerate a non-zero RPO, I can move persistence to a separate thread and batch writes, keeping disk I/O off the trading hot path.
For recovery, I would periodically snapshot the current state and keep an append-only log after the snapshot, so we don't have to replay the entire history.

Consumer 挂了怎么办？我会给 consumer 维护一个 heartbeat 或 progress indicator，比如 如果过了一段时间：producer keeps advancing consumerLastProcessedSequence 不再变化
I would monitor not only thread liveness, but also processing progress. A stuck consumer is effectively failed even if the thread is technically still running.

如果系统要求 high availability，就需要 backup consumer。 不能简单让两个 consumer 同时从同一个 SPSC queue 读。

What if one strategy produces far more signals than all the others? How do you stop it from starving the other queues?
round-robin polling？consumer 每处理一个 Q1 signal 都要检查另外 19 个 empty queue，效率不好。所以可以使用 bounded batching： I would use bounded batching. The consumer can process up to N signals from one queue before moving to the next. This amortizes polling overhead while preventing a busy strategy from monopolizing the consumer.

Strategy 1 is producing 800K signals/sec and the other 19 strategies together produce 700K/sec.Your single consumer can only process 1M/sec.
单纯把 queue 变大不能解决问题，只能延迟爆满。更好的第一选择通常是：不要拆同一个 instrument，先做 dynamic shard assignment。也就是给 hot instrument 一个 dedicated consumer/core。例如原来：

Shard 0: AAPL + IBM + META
Shard 1: MSFT + AMZN
Shard 2: NVDA + TSLA

发现 AAPL 很热以后，可以调整成：

Shard 0: AAPL only
Shard 1: IBM + META + MSFT
Shard 2: AMZN + NVDA + TSLA

But AAPL alone generates more traffic than one consumer can handle. What then? 这时候你应该优化单 shard：reduce allocations / reduce copies / batch work / precompute / better cache locality / avoid locks / CPU pinning /faster data structures

Suppose AAPL currently belongs to Shard 0, but we want to move it to Shard 1 at runtime because Shard 0 is overloaded. How do you migrate ownership without losing, duplicating, or reordering AAPL signals? 这里关键是：不能直接改 hash mapping。

What if the strategy and execution engine need to run in different processes on the same machine? Shared Memory 两个 process 把同一块 physical memory map 到自己的 virtual address space。 Strategy 可以写 ring buffer，Execution 可以直接读。

Now Strategy is on Machine A and Execution Engine is on Machine B. Shared memory no longer works. How do you communicate?
最自然的第一版是：Strategy Process / Network Socket / NIC / Network / NIC / Execution Process 如果 requirement 是：every signal must be processed 我会优先考虑 TCP，或者交易系统内部自己定义的可靠协议。发送前变成固定格式 bytes：[sequence][instrumentId][targetPosition]

TCP already guarantees reliable delivery. Why would you still need sequence numbers and application-level acknowledgements? 因为 TCP 保证的是 byte stream 的 transport-level reliability，不是你的业务操作已经完成。不能说明：Execution application 已经 parse signal 已经 risk check 已经更新 state 已经产生 order 所以如果业务要求知道 processed 到哪里了，需要 application-level ACK： sequence number 是我们的 business/application sequence。
 
## 1. Problem Definition

设计一个 **Trading Position Signal Queue**：

```text
Market / Trading Events
        |
        v
Position Calculator
        |
        | produces PositionSignal
        v
+----------------------+
| Position Signal Queue|
+----------------------+
        |
        v
Strategy / Risk / UI / Execution
```

Signal 可以定义为：

```cpp
struct PositionSignal {
    int accountId;
    int instrumentId;
    long position;
    long timestamp;
};
```

例如：

```text
AAPL position: 100
AAPL position: 120
MSFT position: -50
AAPL position: 80
```

Producer 不断产生 position signal，consumer 从 queue 中读取并处理。

---

# 2. Requirement Clarification

面试中不要立刻开始写 queue，先确认几个 requirement：

1. **Single producer or multiple producers?**
2. **Single consumer or multiple consumers?**
3. **Does every signal need to be processed?**
4. **Can stale position updates be dropped?**
5. **Do we care more about throughput or latency?**
6. **What happens if the consumer is slower than the producer?**
7. **Is the signal an absolute position or a position delta?**
8. **Do multiple consumers compete for messages or does every consumer need every message?**
9. **Do we need persistence / crash recovery?**

这些 requirement 会直接决定 queue 的设计。

---

# 3. Version 1 — Simple Blocking Queue

假设：

```text
Multiple Trading Threads
        |
        v
   Position Queue
        |
        v
Single Risk Thread
```

第一版可以使用：

```text
std::queue
+ mutex
+ condition_variable
```

实现：

```cpp
#include <condition_variable>
#include <mutex>
#include <queue>

struct PositionSignal {
    int accountId;
    int instrumentId;
    long position;
    long timestamp;
};

class PositionSignalQueue {
public:
    void push(PositionSignal signal) {
        {
            std::lock_guard<std::mutex> lock(mutex_);
            queue_.push(std::move(signal));
        }

        cv_.notify_one();
    }

    PositionSignal pop() {
        std::unique_lock<std::mutex> lock(mutex_);

        cv_.wait(lock, [this] {
            return !queue_.empty();
        });

        PositionSignal signal = std::move(queue_.front());
        queue_.pop();

        return signal;
    }

private:
    std::queue<PositionSignal> queue_;
    std::mutex mutex_;
    std::condition_variable cv_;
};
```

面试中可以解释：

> I'll start with a mutex-protected blocking queue because it's simple and correct. Then we can optimize based on the latency and throughput requirements.

不要一开始就使用最复杂的 lock-free 设计。

---

# 4. Bounded Queue and Backpressure

实际系统不能假设 queue 无限大。

假设：

```text
Producer: 2 million signals/sec
Consumer: 1 million signals/sec
```

那么 backlog：

```text
+1 million signals/sec
```

无论 queue 多快，最终都会满。

因此需要定义 **backpressure policy**。

常见方案：

```text
1. Block producer
2. Drop signals
3. Coalesce signals
4. Scale consumers
```

对于 trading position signal，不能简单地说：

```text
queue full -> drop message
```

因为这可能导致 downstream position 错误。

能不能 drop，取决于 signal 的语义。

---

# 5. Absolute State vs Delta

这是这道题非常重要的 distinction。

## Absolute Position

例如：

```text
AAPL position = 100
AAPL position = 110
AAPL position = 120
AAPL position = 130
```

这些消息表示：

```text
Current state
```

如果 consumer 落后：

```text
100 -> 110 -> 120 -> 130
```

很多情况下 consumer 并不需要处理所有 intermediate states。

它只需要：

```text
AAPL position = 130
```

因此可以进行 **coalescing**。

---

## Position Delta

如果消息表示：

```text
BUY 20
SELL 10
BUY 30
```

或者：

```text
+20
-10
+30
```

这是 event/delta。

不能简单丢弃 intermediate messages，因为每个 event 都影响最终 position。

因此面试中应该主动问：

> Does the signal represent an absolute position or a position delta?

这决定了 queue 是否允许 coalescing / dropping stale updates。

---

# 6. Coalescing Queue

如果 signal 是 absolute position，可以按照：

```text
(accountId, instrumentId)
```

进行合并。

例如 producer 产生：

```text
AAPL = 100
AAPL = 110
MSFT = 20
AAPL = 120
```

普通 queue：

```text
[AAPL 100]
[AAPL 110]
[MSFT 20]
[AAPL 120]
```

Coalescing 后：

```text
[AAPL 120]
[MSFT 20]
```

这样 producer 即使短时间远快于 consumer，也不会产生大量 stale updates。

这种设计特别适合：

```text
Position updates
Price updates
Risk state
UI state
Market snapshots
```

---

# 7. Low-Latency Optimization

第一版：

```text
std::queue
+ mutex
+ condition_variable
```

可能存在：

```text
Lock contention
Context switch
Kernel scheduling
Dynamic allocation
Poor latency predictability
```

如果这是 low-latency trading hot path，可以进一步优化。

---

# 8. SPSC Ring Buffer

如果 requirement 是：

```text
Single Producer
Single Consumer
```

最自然的优化是：

**SPSC Ring Buffer**

```text
Producer
   |
   v
+-------------------------+
| [ ][ ][ ][ ][ ][ ][ ]   |
+-------------------------+
   ^                 ^
 read              write
   |
Consumer
```

提前分配固定大小的 contiguous memory。

```cpp
#include <array>
#include <atomic>

template <typename T, size_t N>
class SPSCQueue {
public:
    bool push(const T& value) {
        size_t write =
            write_.load(std::memory_order_relaxed);

        size_t next = (write + 1) % N;

        if (next ==
            read_.load(std::memory_order_acquire)) {
            return false;
        }

        buffer_[write] = value;

        write_.store(
            next,
            std::memory_order_release);

        return true;
    }

    bool pop(T& value) {
        size_t read =
            read_.load(std::memory_order_relaxed);

        if (read ==
            write_.load(std::memory_order_acquire)) {
            return false;
        }

        value = buffer_[read];

        read_.store(
            (read + 1) % N,
            std::memory_order_release);

        return true;
    }

private:
    std::array<T, N> buffer_;

    alignas(64)
    std::atomic<size_t> write_{0};

    alignas(64)
    std::atomic<size_t> read_{0};
};
```

相比 mutex queue：

```text
Preallocated memory
No malloc/free on hot path
No mutex
Contiguous memory
Better cache locality
More predictable latency
```

---

# 9. Why `alignas(64)`?

Producer 主要修改：

```cpp
write_
```

Consumer 主要修改：

```cpp
read_
```

如果它们位于同一个 cache line：

```text
Cache Line
+-------------------------+
| write_ | read_ | ...    |
+-------------------------+
```

两个 CPU core 会不断争夺同一个 cache line。

即使它们修改的是不同变量，也可能产生：

**False Sharing**

使用：

```cpp
alignas(64)
std::atomic<size_t> write_;

alignas(64)
std::atomic<size_t> read_;
```

让两个变量尽量位于不同 cache line，可以减少 cache coherence traffic。

---

# 10. Multiple Producers

假设：

```text
Trading Thread 1 ---\
Trading Thread 2 ----> Position Queue ---> Risk
Trading Thread 3 ---/
Trading Thread 4 --/
```

SPSC 不再适用。

一种方案是：

```text
MPSC Queue
```

但是多个 producer 会竞争：

```text
Shared write index
```

可能导致：

```text
Atomic contention
CAS retries
Cache line bouncing
```

---

# 11. Per-Producer SPSC Queues

Low-latency 系统中可以考虑：

```text
Producer 1 -> SPSC Queue --\
Producer 2 -> SPSC Queue ---\
Producer 3 -> SPSC Queue ----> Consumer
Producer 4 -> SPSC Queue ---/
```

每个 producer 拥有自己的 SPSC queue。

Consumer polling：

```cpp
for (;;) {
    for (auto& queue : queues) {
        PositionSignal signal;

        if (queue.pop(signal)) {
            process(signal);
        }
    }
}
```

优点：

```text
No producer-producer contention
No shared write index
Simple atomic operations
Good cache locality
Predictable latency
```

代价是 consumer 需要 polling 多个 queues。

---

# 12. Multiple Consumers

这里必须进一步确认：

> Are consumers competing for work, or does every consumer need to receive every signal?

这是两种完全不同的模型。

---

## Model A — Work Queue

例如：

```text
             +--> Worker 1
Queue -------+
             +--> Worker 2
```

一个 signal 只需要被其中一个 consumer 处理。

这是：

```text
MPMC Work Queue
```

语义：

```text
Signal -> Consumer A OR Consumer B
```

---

## Model B — Broadcast / Pub-Sub

Trading system 更可能出现：

```text
                 +--> Risk
                 |
Position Signal -+--> Strategy
                 |
                 +--> Logger
                 |
                 +--> UI
```

每个 consumer 都需要看到每个 signal。

普通 MPMC queue 不适合。

应该使用：

**Broadcast / Pub-Sub**

---

# 13. Broadcast Ring Buffer

可以设计：

```text
                    +--> Risk read cursor
                    |
Ring Buffer --------+--> Strategy read cursor
                    |
                    +--> Logger read cursor
```

Producer 维护：

```text
write sequence
```

每个 consumer 维护自己的：

```text
read sequence
```

例如：

```text
Producer sequence = 100

Risk     = 99
Strategy = 97
Logger   = 95
```

Producer 只有在所有 consumer 都处理完某个 slot 后，才能安全覆盖它。

因此 producer 需要关注：

```text
min(all consumer read sequences)
```

这就是典型的：

```text
SPMC Broadcast Ring Buffer
```

---

# 14. Ordering

Position update 通常必须保持 ordering。

例如：

```text
sequence 100:
AAPL position = 100

sequence 101:
AAPL position = 120
```

consumer 不能最后处理成：

```text
120
100
```

因此 signal 最好带 sequence number：

```cpp
struct PositionSignal {
    int accountId;
    int instrumentId;

    long position;

    uint64_t sequence;
    uint64_t timestamp;
};
```

不要完全依赖 timestamp 进行 ordering。

Sequence number 更适合：

```text
Ordering
Gap detection
Duplicate detection
Replay
Recovery
```

---

# 15. Gap Detection

例如 consumer 收到：

```text
100
101
103
```

那么可以发现：

```text
Expected = 102
Received = 103
```

说明：

```text
Signal 102 missing
```

Consumer 可以选择：

```text
Request snapshot
Replay missing events
Reconnect
Stop trading / trigger risk control
```

具体行为取决于系统 requirement。

---

# 16. Crash Recovery

如果 queue 只是：

```text
In-memory queue
```

process crash 后数据会丢失。

但这不一定是问题。

如果 position 可以从 authoritative service 重新获取：

```text
Process restart
      |
      v
Load latest position snapshot
      |
      v
Resume processing
```

那么 in-memory ring buffer 可能已经足够。

---

# 17. Durable Queue

如果 requirement 是：

> Every position update must survive process crashes.

那么需要 persistent storage / durable log。

例如：

```text
Trade Events
     |
     v
Durable Log
     |
     v
Position Engine
     |
     v
Position Signal Queue
     |
     v
Consumers
```

这时候才需要考虑：

```text
Persistent log
Kafka
WAL
Replay
Checkpoint
Snapshot
```

不要一开始就直接引入 Kafka。

先确认 persistence requirement。

---

# 18. Recommended Interview Progression

如果 interviewer 只说：

> Design a trading position signal queue.

可以按照下面的 progression：

```text
Requirement Clarification
        |
        v
Simple Blocking Queue
        |
        v
Bounded Queue
        |
        v
Backpressure
        |
        v
Absolute State vs Delta
        |
        v
Coalescing Stale Updates
        |
        v
Latency Requirement
        |
        v
SPSC Ring Buffer
        |
        v
Multiple Producers
        |
        v
MPSC vs Per-Producer SPSC
        |
        v
Multiple Consumers
        |
        v
Work Queue vs Broadcast
        |
        v
Ordering / Sequence Number
        |
        v
Gap Detection
        |
        v
Crash Recovery / Persistence
```

---

# 19. Most Important Follow-Ups

## Follow-up 1: Absolute Position or Delta?

```text
Absolute state:
100 -> 120 -> 150
```

可以 potentially coalesce：

```text
150
```

但是：

```text
Delta:
+20 -> -10 -> +30
```

不能随便 drop。

---

## Follow-up 2: What If Consumer Is Slower?

不能使用无限 queue。

需要：

```text
Bounded queue
+
Backpressure policy
```

根据 signal semantics 决定：

```text
Block producer
Drop
Coalesce
Scale consumer
```

---

## Follow-up 3: Multiple Consumers

必须区分：

```text
Work distribution
```

和：

```text
Broadcast
```

Work distribution：

```text
Signal -> Consumer A OR Consumer B
```

Broadcast：

```text
Signal -> Consumer A
       -> Consumer B
       -> Consumer C
```

Trading position signal 很可能属于后者。

---

# 20. Final Architecture Example

对于一个 low-latency trading position distribution system，可以最终演进成：

```text
 Trading Thread 1
       |
       v
   SPSC Queue ----\
                   \
 Trading Thread 2   \
       |             \
       v              \
   SPSC Queue ---------> Position Aggregator
                              |
                              | PositionSignal
                              v
                     Broadcast Ring Buffer
                       /       |       \
                      /        |        \
                     v         v         v
                   Risk     Strategy    Logger
```

其中：

```text
Producer side:
Per-thread SPSC queues
        ↓
avoid producer contention

Position Aggregator:
maintains authoritative position state
        ↓
generates absolute PositionSignal

Distribution:
Broadcast ring buffer
        ↓
each consumer maintains its own read sequence

Reliability:
sequence number + gap detection
        ↓
snapshot / replay when necessary
```

---

# 21. Interview Summary

这道题最值得主动讨论的不是：

```text
"Should I use a lock-free queue?"
```

而是：

### 1. Signal Semantics

```text
Absolute state vs delta
```

决定能不能 drop / coalesce。

### 2. Backpressure

```text
What happens when producer > consumer?
```

Queue 不可能无限增长。

### 3. Consumer Semantics

```text
Work queue vs broadcast
```

决定 MPMC queue 和 broadcast ring buffer 哪个正确。

### 4. Latency

从：

```text
mutex + condition_variable
```

逐步优化到：

```text
preallocated SPSC ring buffer
```

而不是一开始过度设计。

### 5. Correctness

需要考虑：

```text
Ordering
Sequence number
Gap detection
Duplicate detection
Recovery
```

### 6. Persistence

最后才讨论：

```text
In-memory
vs
Durable log / Kafka / WAL
```

核心面试思路：

> Start with the simplest correct design, identify the bottleneck or new requirement, and evolve the architecture step by step.
>
> # Trading Position Signal Queue — Follow-up Questions

## 1. How Do You Guarantee Ordering?

首先需要确认：

> Do we need global ordering, or only per-account / per-instrument ordering?

通常没有必要保证所有 signal 的 global ordering。

例如：

```text
AAPL sequence = 100
MSFT sequence = 200
```

我们通常不关心 AAPL 和 MSFT 谁先处理，只需要保证：

```text
AAPL:
100 -> 101 -> 102

MSFT:
200 -> 201 -> 202
```

因此更合理的是：

**Per-key ordering**

key 可以是：

```text
(accountId, instrumentId)
```

---

### Partitioning

可以根据 key hash：

```cpp
partition =
    hash(accountId, instrumentId) % numPartitions;
```

架构：

```text
                    +--> Queue 0 --> Consumer 0
Producer ---------->+--> Queue 1 --> Consumer 1
                    +--> Queue 2 --> Consumer 2
```

保证同一个：

```text
(accountId, instrumentId)
```

始终进入同一个 partition。

这样：

```text
AAPL #100
AAPL #101
AAPL #102
```

不会被不同 consumer 并行处理而 reorder。

---

## 2. Do We Need Global Ordering?

通常：

```text
NO
```

因为 global ordering 会限制 scalability。

如果所有 signal 都必须经过：

```text
One Global Sequence
        ↓
One Ordered Queue
        ↓
Consumer
```

系统很容易形成 bottleneck。

更好的设计：

```text
Partition 0:
AAPL #1 -> #2 -> #3

Partition 1:
MSFT #1 -> #2 -> #3

Partition 2:
NVDA #1 -> #2 -> #3
```

只保证：

```text
per-key ordering
```

这样多个 partition 可以并行处理。

面试中可以说：

> I would avoid global ordering unless it is explicitly required. Usually per-account or per-instrument ordering is enough and allows us to partition the workload.

---

# 3. How Do You Handle Duplicate Signals?

Duplicate 可能来自：

```text
Producer retry
Network retry
Consumer replay
Crash recovery
At-least-once delivery
```

例如：

```text
sequence = 100
sequence = 101
sequence = 101   <- duplicate
sequence = 102
```

可以在 signal 中加入：

```cpp
struct PositionSignal {
    int accountId;
    int instrumentId;

    long position;

    uint64_t sequence;
    uint64_t eventId;
};
```

Consumer 保存：

```text
lastProcessedSequence
```

如果：

```text
received sequence <= lastProcessedSequence
```

可能是 duplicate。

---

# 4. Absolute State Makes Idempotency Easier

假设 signal 是：

```text
AAPL position = 100
```

处理两次：

```text
position = 100
position = 100
```

结果仍然：

```text
100
```

这种操作天然比较容易做到：

**Idempotent**

---

但如果 signal 是 delta：

```text
AAPL +100
```

处理两次：

```text
0
+100
+100

= 200  ❌
```

正确应该是：

```text
100
```

因此 delta event 通常需要：

```text
eventId
sequence number
deduplication
```

---

# 5. Delivery Semantics

系统可能提供：

### At-Most-Once

```text
Signal 最多处理一次
```

但是 crash 时可能丢。

---

### At-Least-Once

```text
Signal 至少处理一次
```

不会轻易丢，但可能 duplicate。

因此 consumer 应该：

```text
idempotent
+
deduplication
```

---

### Exactly-Once

理想语义：

```text
每个 signal 恰好处理一次
```

但是 distributed system 中实现成本很高。

通常更现实的方案：

```text
At-Least-Once
        +
Idempotent Consumer
        +
Deduplication
```

---

# 6. What Happens If Producer Crashes?

例如：

```text
Producer
   |
   | write signal #100
   v
Queue

Producer crashes before receiving acknowledgement
```

Producer 不知道：

```text
signal #100
```

到底成功没有。

restart 后可能 retry：

```text
signal #100
```

于是：

```text
#100
#100
```

产生 duplicate。

因此最好使用：

```text
eventId
sequence number
idempotent consumer
```

---

# 7. What Happens If Consumer Crashes?

假设：

```text
#100
#101
#102
#103
```

Consumer 已经处理到：

```text
#101
```

然后 crash。

如果是 durable queue，可以记录：

```text
checkpoint = 101
```

restart：

```text
read checkpoint
      ↓
start from #102
```

如果使用 at-least-once delivery，也可能重新收到：

```text
#101
```

因此 consumer 应该能够识别 duplicate。

---

# 8. What If One Instrument Is Extremely Hot?

例如：

```text
AAPL = 80% traffic
```

简单：

```cpp
hash(instrumentId) % N
```

可能得到：

```text
Partition 0: AAPL 80%
Partition 1: 5%
Partition 2: 8%
Partition 3: 7%
```

那么：

```text
Partition 0 overloaded
```

这就是：

**Hot-key problem**

---

可能的解决方案：

```text
better partitioning key

(accountId, instrumentId)
```

而不是只使用：

```text
instrumentId
```

例如：

```text
Account A + AAPL -> Queue 0
Account B + AAPL -> Queue 1
Account C + AAPL -> Queue 2
```

但必须考虑：

> What ordering guarantee do we actually need?

如果要求整个 AAPL global ordering，就不能随便拆。

---

# 9. How Do You Scale Consumers?

最直接的方法：

```text
                    +--> Queue 0 --> Consumer 0
Producer ---------->+--> Queue 1 --> Consumer 1
                    +--> Queue 2 --> Consumer 2
                    +--> Queue 3 --> Consumer 3
```

按照：

```text
hash(accountId, instrumentId)
```

partition。

优点：

```text
parallel processing
per-key ordering
less lock contention
better cache locality
```

---

# 10. Work Queue vs Broadcast

Multiple consumers 有两种完全不同的语义。

## Work Distribution

```text
             +--> Consumer A
Queue -------+
             +--> Consumer B
```

一个 signal：

```text
Consumer A OR Consumer B
```

例如 worker pool。

---

## Broadcast

```text
                 +--> Risk
                 |
Position Signal -+--> Strategy
                 |
                 +--> Logger
                 |
                 +--> UI
```

每个 consumer 都需要收到 signal：

```text
Consumer A AND Consumer B AND Consumer C
```

这时候普通 MPMC queue 不够。

可以使用：

```text
Pub/Sub
Broadcast Ring Buffer
Independent Consumer Queues
```

---

# 11. What If One Broadcast Consumer Is Slow?

假设：

```text
Producer sequence = 1000

Risk     cursor = 999
Strategy cursor = 995
Logger   cursor = 500
```

如果 ring buffer 需要等待所有 consumer：

```text
minRead = 500
```

Logger 会拖慢整个系统。

这是：

**Slow Consumer Problem**

---

可能的策略：

### Option 1 — Block Producer

```text
Slow Consumer
     ↓
Ring Buffer Full
     ↓
Producer blocks
```

适合不能丢数据的系统，但可能影响 trading hot path。

---

### Option 2 — Disconnect Slow Consumer

例如 Logger 太慢：

```text
Logger falls too far behind
        ↓
disconnect
        ↓
recover from durable log later
```

---

### Option 3 — Independent Queue

```text
                   +--> Risk Queue
Producer ----------+--> Strategy Queue
                   +--> Logger Queue
```

Logger 慢不会直接 block Risk。

代价：

```text
more memory
more copies
more complexity
```

---

### Option 4 — Durable Log Catch-Up

```text
Fast consumers:
Ring Buffer

Slow consumer:
Durable Log
      ↓
Replay later
```

这是比较常见的思路。

---

# 12. How Large Should the Queue Be?

不能简单说：

```text
As large as possible
```

应该根据：

```text
Peak producer rate
Consumer rate
Expected burst duration
Message size
Memory budget
```

估算。

例如：

```text
Peak producer = 2M/s
Consumer      = 1.5M/s

Difference = 500K/s
```

如果需要吸收：

```text
200 ms burst
```

需要：

```text
500K * 0.2
= 100K signals
```

因此：

```text
capacity ≈
(producer peak - consumer capacity)
*
expected burst duration
```

然后增加 safety margin。

---

# 13. What Happens When Queue Is Almost Full?

不要等：

```text
100% full
```

才发现问题。

可以设置 threshold：

```text
Queue Usage

70% -> warning metric
85% -> throttle / alert
95% -> emergency policy
100% -> block / spill / reject
```

具体 threshold 根据系统 requirement 决定。

---

# 14. What Metrics Would You Monitor?

至少应该监控：

```text
queue depth

enqueue rate

dequeue rate

consumer lag

oldest message age

end-to-end latency

p50 latency

p99 latency

p99.9 latency

drop count

coalesce count

duplicate count

replay count

queue-full count
```

---

### Queue Depth

例如：

```text
capacity = 100K
current depth = 80K
```

说明 backlog 很高。

---

### Consumer Lag

例如：

```text
Producer sequence = 1,000,000
Consumer sequence =   950,000

lag = 50,000
```

---

### Oldest Message Age

例如：

```text
oldest signal has been waiting 500 ms
```

这有时候比：

```text
queue depth
```

更能直接反映 consumer 是否跟不上。

---

# 15. How Do You Timestamp Signals?

需要区分：

```text
Wall Clock
vs
Monotonic Clock
```

---

## Wall Clock

例如：

```text
2026-08-16 12:30:00
```

适合：

```text
logging
human-readable timestamps
cross-system business timestamps
```

但是可能受到：

```text
NTP adjustment
clock correction
```

影响。

---

## Monotonic Clock

只保证：

```text
time always moves forward
```

更适合：

```text
latency measurement
timeout
elapsed time
```

例如：

```cpp
auto start =
    std::chrono::steady_clock::now();
```

---

# 16. Lock-Based vs Lock-Free

不要直接回答：

```text
Lock-free is always faster.
```

这是不准确的。

第一版可以：

```text
std::mutex
+
condition_variable
```

优点：

```text
simple
correct
easy to maintain
```

如果 benchmark 证明：

```text
lock contention
context switch
tail latency
```

是瓶颈，再考虑 lock-free。

---

## SPSC

对于：

```text
Single Producer
Single Consumer
```

lock-free ring buffer 非常适合：

```text
simple atomic operations
no lock contention
preallocated memory
predictable latency
```

---

## MPMC

MPMC lock-free queue 会复杂很多：

```text
CAS loops
memory ordering
ABA issues
cache-line contention
memory reclamation
```

所以不要为了：

```text
"lock-free sounds fast"
```

就直接使用。

---

# 17. Busy Polling vs Condition Variable

## Condition Variable

```text
No data
   ↓
Consumer sleeps
   ↓
Producer notify
   ↓
OS wakes consumer
   ↓
Consumer scheduled
```

优点：

```text
low CPU usage
```

缺点：

```text
context switch
scheduler latency
wake-up latency
```

---

## Busy Polling

```cpp
while (running) {
    PositionSignal signal;

    if (queue.pop(signal)) {
        process(signal);
    }
}
```

Consumer：

```text
check
check
check
check
signal arrives
process immediately
```

优点：

```text
very low wake-up latency
predictable latency
```

缺点：

```text
burns CPU
```

---

## Hybrid

也可以：

```text
spin for a while
       ↓
still empty?
       ↓
sleep
```

即：

```text
busy spin
   +
condition variable
```

在 latency 和 CPU usage 之间做 trade-off。

---

# 18. Memory Allocation

Trading hot path 上通常希望避免：

```cpp
new
delete
malloc
free
```

因为可能带来：

```text
allocator contention
cache miss
unpredictable latency
fragmentation
```

可以使用：

```text
Preallocated Ring Buffer
Memory Pool
Object Pool
Fixed-size Message
```

例如：

```cpp
std::array<PositionSignal, 65536> buffer;
```

启动时一次性分配。

---

# 19. Cache Locality

Ring buffer 使用 contiguous memory：

```text
[S0][S1][S2][S3][S4][S5]
```

相比：

```text
Node -> Node -> Node -> Node
```

通常 cache locality 更好。

因此 low-latency queue 常使用：

```text
array
ring buffer
preallocated contiguous storage
```

而不是 linked list。

---

# 20. False Sharing

假设：

```cpp
std::atomic<size_t> read;
std::atomic<size_t> write;
```

它们位于同一个 cache line：

```text
Cache Line
+----------------------+
| read | write | ...   |
+----------------------+
```

Producer 修改：

```text
write
```

Consumer 修改：

```text
read
```

两个 core 会不断争夺同一个 cache line。

这就是：

**False Sharing**

可以：

```cpp
alignas(64)
std::atomic<size_t> read;

alignas(64)
std::atomic<size_t> write;
```

尽量把它们放在不同 cache line。

---

# 21. What If Signal Size Becomes Large?

如果：

```cpp
struct PositionSignal {
    ...
    char hugeData[10000];
};
```

每次：

```cpp
queue.push(signal);
```

都会产生大量 copy。

可以考虑：

```text
move semantics
index / handle
preallocated object pool
```

例如 queue 中只保存：

```cpp
uint32_t signalIndex;
```

真正对象放在：

```text
Preallocated Signal Pool
```

但这样需要额外处理：

```text
ownership
lifetime
memory reclamation
```

所以只有 signal 确实很大时才值得增加这种复杂度。

---

# 22. How Do You Shut Down Safely?

不能简单：

```text
kill consumer thread
```

因为 queue 中可能还有：

```text
unprocessed signals
```

Graceful shutdown：

```text
Stop accepting new signals
        ↓
Drain queue
        ↓
Finish processing
        ↓
Flush / checkpoint
        ↓
Stop consumer
        ↓
Join threads
```

C++ 中通常：

```cpp
running_ = false;
cv_.notify_all();

for (auto& thread : threads_) {
    thread.join();
}
```

---

# 23. How Do You Restart Without Losing Signals?

如果需要 rolling restart：

```text
Old Consumer
     ↓
stop receiving new work
     ↓
drain current queue
     ↓
checkpoint
     ↓
shutdown
```

新 consumer：

```text
startup
   ↓
load checkpoint / snapshot
   ↓
resume
```

如果有 durable log：

```text
checkpoint = 1000

restart
   ↓
replay from 1001
```

---

# 24. What Consistency Does Each Consumer Need?

不同 consumer 可能有完全不同的 requirement。

例如：

## UI

```text
Only latest position matters
```

可以：

```text
coalesce
eventual consistency
```

---

## Strategy

可能要求：

```text
low latency
per-instrument ordering
```

---

## Risk

可能要求：

```text
no missed risk-limit transition
strong ordering
low latency
```

---

## Audit

可能要求：

```text
every event
durability
replay
no loss
```

因此：

```text
One queue policy for every consumer
```

不一定是正确设计。

可以设计：

```text
                         +--> Strategy
                         |    low latency
                         |
Position Engine ---------+--> Risk
                         |    ordered / lossless
                         |
                         +--> UI
                         |    coalesced
                         |
                         +--> Audit
                              durable
```

---

# 25. Persistence / Crash Recovery

首先确定：

> Is the PositionSignal itself the source of truth?

如果不是，例如：

```text
Durable Trade / Fill Log
        ↓
Position Engine
        ↓
PositionSignal
```

PositionSignal 只是：

```text
derived state
```

那么 signal queue 可以保持：

```text
in-memory
```

crash 后：

```text
restart
   ↓
load position snapshot
or
replay fills
   ↓
rebuild position
```

---

如果 PositionSignal 本身必须永久保存：

```text
Producer
   ↓
Durable Log / WAL
   ↓
Consumer
   ↓
Checkpoint
```

restart：

```text
read checkpoint
      ↓
replay missing signals
```

---

# 26. Async — Does It Solve Slow Consumers?

Async 可以：

```text
Producer
   ↓
Queue
   ↓
Consumer
```

让 producer 不需要同步等待：

```text
consumer.process()
```

因此它解决：

**Producer/Consumer Coupling**

但如果：

```text
Producer = 2M/s
Consumer = 1M/s
```

async 后仍然：

```text
t = 1 sec -> backlog 1M
t = 2 sec -> backlog 2M
t = 10 sec -> backlog 10M
```

最终：

```text
Queue Full
```

所以：

> Async solves coupling; it does not solve capacity.

最终仍然需要：

```text
Backpressure
Coalescing
Partitioning
More Consumers
Batch Processing
Consumer Optimization
```

---

# 27. What If Consumer Is Permanently Slower?

如果只是 temporary burst：

```text
Producer temporarily > Consumer
        ↓
Queue absorbs burst
        ↓
Producer rate drops
        ↓
Consumer catches up
```

没问题。

但是：

```text
Producer permanently = 2M/s
Consumer permanently = 1M/s
```

任何 queue 都无法解决。

必须：

```text
Increase consumer throughput
        |
        +--> optimize processing
        |
        +--> batching
        |
        +--> partition workload
        |
        +--> add consumers
        |
        +--> reduce/coalesce messages
        |
        +--> throttle producer
```

核心结论：

> A queue can absorb temporary bursts, but it cannot solve a permanent throughput mismatch.

---

# 28. Batch Processing

如果 throughput 比 single-message latency 更重要，可以 batch：

```text
Queue

S1
S2
S3
...
S100
   ↓
Consumer processes 100 together
```

例如：

```cpp
std::vector<PositionSignal> batch;

queue.popBatch(batch, 100);

for (const auto& signal : batch) {
    process(signal);
}
```

可以 amortize：

```text
locking
atomic operations
syscalls
network I/O
```

提高 throughput。

代价：

```text
latency may increase
```

因为第一条 signal 可能需要等待 batch。

---

# 29. Throughput vs Latency

如果更关注 latency：

```text
SPSC Ring Buffer
Preallocation
Busy Polling
CPU Affinity
No Dynamic Allocation
Small Batches / No Batching
```

如果更关注 throughput：

```text
Batch Processing
Multiple Consumers
Partitioning
Larger Buffers
Amortize Synchronization Cost
```

因此应该先问：

> Is this queue on the critical trading path, or is it for downstream risk, UI, logging, or reporting?

不同 workload 的设计可能完全不同。

---

# 30. Most Important Follow-Ups to Remember

如果时间有限，重点准备下面这些。

### Ordering

```text
Global ordering?
or
Per-key ordering?
```

通常选择：

```text
per-account / per-instrument ordering
```

然后通过 partitioning 保证。

---

### Duplicate / Delivery Semantics

准备：

```text
At-most-once
At-least-once
Exactly-once
Idempotency
Sequence Number
Event ID
```

---

### Slow Consumer

准备：

```text
Bounded Queue
Backpressure
Coalescing
Spill
Scale Consumer
```

并记住：

> Queue solves bursts, not permanent throughput mismatch.

---

### Partitioning

准备：

```text
hash(accountId, instrumentId)
        %
numPartitions
```

同一个 key 固定进入同一个 partition，从而：

```text
maintain ordering
+
scale consumers
```

---

### Observability

至少记住：

```text
Queue Depth
Consumer Lag
Oldest Message Age
Enqueue / Dequeue Rate
p99 / p99.9 Latency
Drop / Coalesce Count
```

---

### Low-Latency C++

准备：

```text
SPSC Ring Buffer
Busy Polling
Preallocation
Cache Locality
False Sharing
alignas(64)
Atomic Memory Ordering
CPU Affinity
```

---

# 31. Final Interview Framework

拿到：

> Design a Trading Position Signal Queue

可以按照下面顺序展开：

```text
1. Clarify Requirements
        ↓
2. Signal Semantics
   Absolute State vs Delta
        ↓
3. Simple Bounded Queue
        ↓
4. Producer > Consumer?
   Backpressure / Coalescing
        ↓
5. Latency Requirement
   Blocking vs Busy Polling
        ↓
6. SPSC Ring Buffer
        ↓
7. Multiple Producers
   MPSC vs Per-Producer SPSC
        ↓
8. Multiple Consumers
   Work Queue vs Broadcast
        ↓
9. Ordering
   Per-Key Sequence
        ↓
10. Partitioning
        ↓
11. Duplicate / Idempotency
        ↓
12. Consumer Failure
    Checkpoint / Replay
        ↓
13. Slow Consumer
        ↓
14. Persistence
    Snapshot / WAL / Durable Log
        ↓
15. Observability
        ↓
16. Capacity Planning
```

最终思路不是一上来就说：

```text
Kafka
Lock-Free
Distributed System
```

而是：

> Start with the simplest correct design. Then evolve the architecture as new requirements are introduced.

每一个优化都应该能够回答：

```text
What problem am I solving?
Why is the previous design insufficient?
What trade-off am I introducing?
```

这才是这道 system design 题真正应该展示的设计过程。

