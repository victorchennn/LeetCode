template<typename T>
    class ConcurrentQueue {
        private:
            std::queue<T> q;
            mutable std::mutex m;
            std::condition_variable cv; // blockingQueue, size limit? condition_variable notFull;

        public:
            void push(T value) {
                {
                    std::lock_guard<std::mutex> lock(m); 
                    q.push(std::move(value)); // q.push(value) is copy, but std::move(value) is Rvalue so use push(T&&)
                }

                cv.notify_one(); // wake up, lock mutex
            }

            bool pop(T& value) {
                std::lock_guard<std::mutex> lock(m);
                if (q.empty())
                    return false;

                // std::unique_lock<std::mutex> lock(m); // lock_guard 不能主动 unlock()
                // 
                // cv.wait(lock, [&] {
                //     return !q.empty(); // if q is empty, unlock mutex, keep sleeping, no use of CPU
                // });
                // same as 
                // while (q.empty()) {
                //     cv.wait(lock);
                // }


                value = std::move(q.front());
                q.pop();
                return true;
            }

            bool empty() const {
                std::lock_guard<std::mutex> lock(m);
                return q.empty();
            }
        
    };

class BlockingQueue {
public:
    explicit BlockingQueue(size_t capacity)
        : capacity_(capacity) {}

    void push(PositionSignal signal) {
        std::unique_lock<std::mutex> lock(mutex_);

        notFull_.wait(lock, [this] {
            return queue_.size() < capacity_;
        });

        queue_.push(std::move(signal));

        lock.unlock();
        notEmpty_.notify_one();
    }

    PositionSignal pop() {
        std::unique_lock<std::mutex> lock(mutex_);

        notEmpty_.wait(lock, [this] {
            return !queue_.empty();
        });

        PositionSignal signal = std::move(queue_.front());
        queue_.pop();

        lock.unlock();
        notFull_.notify_one();

        return signal;
    }

private:
    size_t capacity_;
    std::queue<PositionSignal> queue_;

    std::mutex mutex_;
    std::condition_variable notEmpty_;
    std::condition_variable notFull_;
};
