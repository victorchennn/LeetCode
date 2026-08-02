class MyCircularQueue {
private:
    std::vector<int> data;
    int head;
    int tail;
    int len;

public:
    MyCircularQueue(int k)
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

    alignas(64) // multiple? fetch_add(1, std::memory_order_acq_rel);
    std::atomic<std::size_t> writeCounter_{0};

public:
    bool push(const T& value) {
        const std::size_t write = writeCounter_.load(std::memory_order_relaxed); // 最弱也是最快 只保证atomic操作本身是原子的
        const std::size_t read = readCounter_.load(std::memory_order_acquire);
        if (write - read == capacity_) {
            return false;
        }

        buffer_[write % capacity_] = value;
        writeCounter_.store(write + 1, std::memory_order_release); // 在store之前的所有写操作，都不能被移动到store后面

        return true;
    }

    bool pop(T& value) {
        const std::size_t read = readCounter_.load(std::memory_order_relaxed);
        const std::size_t write = writeCounter_.load(std::memory_order_acquire); // 在load之后的读写不能被移动到load前面
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
