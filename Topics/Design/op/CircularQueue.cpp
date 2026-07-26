class MyCircularQueue {
private:
    std::vector<int> data;
    int head;
    int tail;
    int len;

public:
    MyCircularQueue(int k)
        : data(k), head(0), tail(-1), len(0) {}

    bool enQueue(int value) {
        if (isFull()) {
            return false;
        }

        tail = (tail + 1) % data.size();
        data[tail] = value;
        ++len;
        return true;
    }

    bool deQueue() {
        if (isEmpty()) {
            return false;
        }

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

struct FastQueue {
    alignas(CACHE_LINE_SIZE)
    std::atomic<std::uint64_t> writerCounter{0};
    std::atomic<std::uint64_t> publishedCounter{0};

    std::byte* buffer;
    std::size_t capacity;
};

class Producer {
private:
    FastQueue& queue;
    std::uint64_t localCounter = 0;

public:
    void write(std::span<const std::byte> msg) {
        const std::int32_t messageSize = Align<64>(message.size());
        const std::uint64_t recordSize = sizeof(messageSize) + message.size();
    
        const std::uint64_t newCounter = localCounter + recordSize;
    
        // 先预留
        queue.writerCounter.store(newCounter, std::memory_order_release);
    
        std::memcpy(queue.buffer + localCounter, &messageSize, sizeof(messageSize));
        std::memcpy(queue.buffer + localCounter + sizeof(messageSize), message.data(), message.size());
    
        // 全部写完以后再发布
        queue.publishedCounter.store(newCounter, std::memory_order_release);
    
        localCounter = newCounter;
    }
};

class Consumer {
private:
    FastQueue& queue;
    std::uint64_t localCounter = 0;
    std::uint64_t cachedPublishedCounter = 0;

public:
    std::int32_t tryRead(std::span<std::byte> destination) {
        if (localCounter == cachedPublishedCounter) {
            cachedPublishedCounter =
                queue.publishedCounter.load(
                    std::memory_order_acquire
                );
        }
    
        if (localCounter == cachedPublishedCounter) {
            return 0;
        }
    
        std::int32_t messageSize = 0;
    
        std::memcpy(&messageSize, queue.buffer + localCounter, sizeof(messageSize));
        std::memcpy(destination.data(), queue.buffer + localCounter + sizeof(messageSize), messageSize);
    
        localCounter += sizeof(messageSize) + messageSize;
        return messageSize;
    }
};
