// 如果任务提交速度远大于执行速度怎么办？Bounded Queue
// 每个 worker 一个自己的 queue。

class ThreadPool {
private:
    std::vector<std::thread> workers_;
    std::queue<std::function<void()>> tasks_;

    std::mutex mutex_;
    std::condition_variable cv_;
    bool stop_{false};

public:
    explicit ThreadPool(std::size_t threadCount) {
        for (std::size_t i = 0; i < threadCount; ++i) {
            workers_.emplace_back([this] {
                while (true) {
                    std::function<void()> task;

                    {
                        std::unique_lock<std::mutex> lock(mutex_);

                        cv_.wait(lock, [this] {
                            return stop_ || !tasks_.empty();
                        });

                        if (stop_ && tasks_.empty()) {
                            return;
                        }

                        task = std::move(tasks_.front());
                        tasks_.pop();
                    }

                    task();
                }
            });
        }
    }

    // std::future if want to return or callback
    void submit(std::function<void()> task) {
        std::lock_guard<std::mutex> lock(mutex_);

        if (stop_) {
            return;
        }
        tasks_.push(std::move(task));
        cv_.notify_one();
    }

    ~ThreadPool() {
        {
            std::lock_guard<std::mutex> lock(mutex_);
            stop_ = true;
        } // has to because 千万不要在持有 mutex 的情况下调用可能阻塞很长时间的操作

        cv_.notify_all();

        for (std::thread& worker : workers_) {
            worker.join(); // 等待这个线程执行结束。
        } 
    }
};
