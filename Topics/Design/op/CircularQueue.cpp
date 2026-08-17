class MyCircularQueue {
private:
    std::vector<int> data; // int* data; int capacity;
    int head;
    int tail;
    int len;

public:
    MyCircularQueue(int k) // data = new int[capacity]; also need ~MyCircularQueue() { delete[] data; }
        : data(k), head(0), tail(0), len(0) {} 

    bool enQueue(int value) {
        if (isFull()) {
            return false;
        }

        data[tail] = value;
        tail = (tail + 1) % data.size();
        ++len;
        return true;
    }

    bool deQueue(int& value) {
        if (isEmpty()) {
            return false;
        }

        value = data[head];
        head = (head + 1) % data.size();
        --len;
        return true;
    }

    int Front() {
        return isEmpty() ? -1 : data[head];
    }

    int Rear() {
        if (isEmpty()) {
            return -1;
        }
    
        int index = (tail - 1 + data.size()) % data.size();
        return data[index];
    }

    bool isEmpty() const {
        return len == 0;
    }

    bool isFull() const {
        return len == static_cast<int>(data.size());
    }
};

int main() {
    MyCircularQueue q(3);

    assert(q.isEmpty());
    assert(!q.isFull());
    assert(q.Front() == -1);
    assert(q.Rear() == -1);

    assert(q.enQueue(10));
    assert(q.enQueue(20));
    assert(q.enQueue(30));

    assert(q.isFull());
    assert(!q.enQueue(40));

    assert(q.Front() == 10);
    assert(q.Rear() == 30);

    int value;

    assert(q.deQueue(value));
    assert(value == 10);
    assert(q.Front() == 20);
    assert(q.Rear() == 30);

    // tail 此时回绕到下标 0
    assert(q.enQueue(40));

    assert(q.isFull());
    assert(q.Front() == 20);
    assert(q.Rear() == 40);

    assert(q.deQueue(value));
    assert(value == 20);

    assert(q.deQueue(value));
    assert(value == 30);

    assert(q.deQueue(value));
    assert(value == 40);

    assert(q.isEmpty());
    assert(!q.deQueue(value));

    std::cout << "All tests passed\n";
}

Single Producer / Multiple Consumer
Broadcast（所有 Consumer 都能看到同一条消息）
无锁（lock-free）
Ring Buffer

       Producer => writeCounter & publishedCounter
           │
  写入 Market Data
           │
   +----------------+
   |   Ring Buffer   |
   +----------------+
     │      │      │
     ▼      ▼      ▼
Strategy1 Strategy2 Strategy3 

Consumer A 读了一条消息，并不会让 Consumer B 看不到。
所以它不是工作队列（Work Queue），而是广播队列（Broadcast Queue）

Queue: store in Byte
+-------------+----------------+
| messageSize | payload        |
+-------------+----------------+

Producer => 
  writeCounter  Producer已经预留到哪里 
  publishedCounter 真正已经写好的位置


template <typename T>
class SpscRingBuffer { // lock-free
private:
    std::vector<T> buffer_;
    const std::size_t capacity_;

    alignas(64) // by default memory_order_seq_cst 
    std::atomic<std::size_t> readCounter_{0}; 
    // std::vector<std::atomic<size_t>> readCounters; 每个 Consumer 一个 readCounter，Producer 看 min(readCounters)

    // Producer-local cache of readCounter_.
    // Not atomic because only producer accesses it.
    // std::size_t cachedReadCounter_{0};

    alignas(64) // multiple? fetch_add(1, std::memory_order_acq_rel);
    std::atomic<std::size_t> writeCounter_{0};

public:
    bool push(const T& value) {
        const std::size_t write = writeCounter_.load(std::memory_order_relaxed); // 最弱也是最快 只保证atomic操作本身是原子的
        const std::size_t read = readCounter_.load(std::memory_order_acquire);
        if (write - read == capacity_) {
            return false;
        }

        
        // if (write - cachedReadCounter_ == capacity_) { // Caching Read Counter
        //     cachedReadCounter_ = readCounter_.load(std::memory_order_acquire);
        
        //     if (write - cachedReadCounter_ == capacity_)
        //         return false;
        // }

        buffer_[write % capacity_] = value;
        writeCounter_.store(write + 1, std::memory_order_release); // it's telling consumer this is ready, like broadcast

        return true;
    }

    bool pop(T& value) {
        const std::size_t read = readCounter_.load(std::memory_order_relaxed);
        const std::size_t write = writeCounter_.load(std::memory_order_acquire); // get to know this position is ready
        if (write == read) {
            return false;
        }

        value = std::move(buffer_[read % capacity_]);
        readCounter_.store(read + 1, std::memory_order_release);

        return true;
    }
};


template <typename T>
class SpmcBroadcastRingBuffer {
private:
    struct alignas(64) ConsumerCounter {
        std::atomic<std::size_t> value{0};
    };

    std::vector<T> buffer_;
    const std::size_t capacity_;

    // Producer 独占写入，但 Consumer 会读取
    alignas(64)
    std::atomic<std::size_t> writeCounter_{0};

    // 每个 Consumer 有自己的读取位置
    std::vector<ConsumerCounter> readCounters_;

public:
    SpmcBroadcastRingBuffer(std::size_t capacity, std::size_t consumerCount)
        : buffer_(capacity),
          capacity_(capacity),
          readCounters_(consumerCount) {}

    bool push(const T& value) {
        const std::size_t write = writeCounter_.load(std::memory_order_relaxed);

        // 找到最慢 Consumer
        std::size_t minRead = write;
        for (auto& counter : readCounters_) {
            const std::size_t read = counter.value.load(std::memory_order_acquire);
            if (read < minRead) {
                minRead = read;
            }
        }

        // 最慢 Consumer 还没有读完，不能覆盖
        if (write - minRead >= capacity_) {
            return false;
        }

        buffer_[write % capacity_] = value;

        // 发布数据：所有 Consumer 都可以看到
        writeCounter_.store(write + 1, std::memory_order_release);
        return true;
    }

    bool pop(std::size_t consumerId, T& value) {
        if (consumerId >= readCounters_.size()) {
            return false;
        }

        auto& readCounter = readCounters_[consumerId].value;

        const std::size_t read = readCounter.load(std::memory_order_relaxed);
        const std::size_t write = writeCounter_.load(std::memory_order_acquire);
        if (read == write) {
            return false;
        }

        value = buffer_[read % capacity_];
        readCounter.store(read + 1, std::memory_order_release);
        return true;
    }
};

compare_exchange_weak( // compare_exchange_strong
    expected,
    desired,
    success_memory_order, // release
    failure_memory_order // relaxed
);

// weak used in a while loop in case to retry if fail
int expected = x.load();
while (!x.compare_exchange_weak(
    expected,
    expected + 1))
{
}

// string is often for a one-shot attempt:
int expected = 10;
if (x.compare_exchange_strong(expected, 11)) {
    // successfully changed 10 -> 11
} else {
    // failed
}

// if multiple producers share a counter: auto pos = nextPosition.fetch_add(1); return old value, but increate it by 1
// 1. 不维护 per-consumer read index
//    ↓
//    Producer 不需要追踪所有 Consumer
// 2. Less contention
//    ↓
//    更少 shared cache-line communication
//    ↓
//    Producer 更快、更稳定
// 3. No backpressure
//    ↓
//    Producer 永远不等 Consumer
//    ↓
//    Slow Consumer 可能 overflow
// 4. Store bytes
//    ↓
//    Ring 可以保存 heterogeneous events
//    Trade / Add / Cancel / ...
// Producer 是最重要的。宁可让一个跟不上的 Consumer overflow 并重新同步，也不要让这个 Consumer 给整个 market-data fan-out 路径施加 backpressure。
// SPMC V1 mIndex; mPendingIndex; Producer不知道 Consumer 在哪。Consumer has its own localIndex Producer 不关心任何 Consumer 的 read position；
// 它只负责 reserve → write → publish。 这也是为什么它可以做到很低的 producer-side contention，但代价就是 slow consumer 可能被覆盖、发生 overflow。
// ┌──────┬───────────────┬──────┬───────────────────┐
// │ size │ message bytes │ size │ message bytes     │
// └──────┴───────────────┴──────┴───────────────────┘
// 24 | <24 bytes Trade>
// 48 | <48 bytes AddOrder>
// 16 | <16 bytes Cancel>
// 先读 size
//    ↓
// 知道这条 message 多长
//    ↓
// 读对应数量的 bytes
//    ↓
// 跳到下一条 message
struct Q {
    alignas(64) std::atomic<uint64_t> mIndex;
    alignas(64) std::atomic<uint64_t> mPendingIndex;
    alignas(64) uint8_t mData[0];
};

// v2
// Ring 每一个 Ring Buffer slot 上放一个 mini Seqlock。
┌─────────┬─────────┬─────────┬─────────┬─────────┐
│ atomic  │ atomic  │ atomic  │ atomic  │ atomic  │
│ Event A │ Event B │ Event C │ Event D │ Event E │
└─────────┴─────────┴─────────┴─────────┴─────────┘
// atomic<uint64_t> counter; 同时表示两个信息。
// 最低 bit：bit 0 是否正在 write / 剩下的 bits：version / generation
Block
┌────────────────────────┐
│ mVersion                │  ← 这个 Block 当前是什么状态
├────────────────────────┤
│ mSize                   │  ← 这里存的 message 多大
├────────────────────────┤
│ mData                   │  ← 真正的数据
│ ...                     │
└────────────────────────┘
Memory
┌───────────────────────────────┐
│ Header                        │
├───────────────────────────────┤
│ mBlockCounter[...]            │
├───────────────────────────────┤
│ Block 0                       │
│ version | size | data         │
├───────────────────────────────┤
│ Block 1                       │
│ version | size | data         │
├───────────────────────────────┤
│ Block 2                       │
│ version | size | data         │
├───────────────────────────────┤
│ ...                           │
└───────────────────────────────┘

               Shared Header
              mBlockCounter
                    │
                    │ reader join 时看一下
                    ▼

Consumer local position
        │
        │ 决定我要读哪个 Block
        ▼
      Block N
        │
        ├── mVersion
        ├── mSize
        └── mData
