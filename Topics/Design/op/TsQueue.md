# Designing a Trading Position Signal Queue

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
