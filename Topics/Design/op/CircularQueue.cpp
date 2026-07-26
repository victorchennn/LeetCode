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
        return isEmpty() ? -1 : data[tail];
    }

    bool isEmpty() const {
        return len == 0;
    }

    bool isFull() const {
        return len == static_cast<int>(data.size());
    }
};

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
    const std::size_t mask_;

    alignas(64)
    std::atomic<std::size_t> readCounter_{0}; // std::vector<std::atomic<size_t>> readCounters; 每个 Consumer 一个 readCounter，Producer 看 min(readCounters)

    

    alignas(64)
    std::atomic<std::size_t> writeCounter_{0};

public:
    bool push(const T& value) {
        const std::size_t write = writeCounter_.load(std::memory_order_relaxed);
        const std::size_t read = readCounter_.load(std::memory_order_acquire);
        if (write - read == capacity_) {
            return false;
        }

        buffer_[write % capacity_] = value;
        writeCounter_.store(write + 1, std::memory_order_release);

        return true;
    }

    bool pop(T& value) {
        const std::size_t read = readCounter_.load(std::memory_order_relaxed);
        const std::size_t write = writeCounter_.load(std::memory_order_acquire);
        if (write == read) {
            return false;
        }

        value = std::move(buffer_[read % capacity_]);
        readCounter_.store(read + 1, std::memory_order_release);

        return true;
    }
};
