class RateLimiter {
public:
    using Clock = std::chrono::steady_clock;
    using TimePoint = Clock::time_point;
    using Duration = Clock::duration;

    RateLimiter(std::size_t maxCalls, Duration period)
        : maxCalls_(maxCalls), period_(period) {}

    // 使用系统单调时钟判断当前调用是否合法
    bool allow() {
        return allowAt(Clock::now());
    }

    // 方便测试：手动传入时间点
    bool allowAt(TimePoint now) {
        std::lock_guard<std::mutex> lock(mutex_);

        removeExpired(now);

        if (acceptedCalls_.size() >= maxCalls_) {
            return false;
        }

        acceptedCalls_.push_back(now);
        return true;
    }

    std::size_t currentCalls() const {
        std::lock_guard<std::mutex> lock(mutex_);
        return acceptedCalls_.size();
    }

private:
    void removeExpired(TimePoint now) {
        // 窗口定义为 (now - period_, now]
        while (!acceptedCalls_.empty() &&
               acceptedCalls_.front() <= now - period_) {
            acceptedCalls_.pop_front();
        }
    }

private:
    std::size_t maxCalls_;
    Duration period_;

    std::deque<TimePoint> acceptedCalls_;
    mutable std::mutex mutex_;
};
