class LogServer {
private:
    struct Log {
        std::string logId;
    };

    int maxLogs_;
    int latestTimestamp_ = -1;
    int logCount_ = 0;

    // timestamp -> logs received at this timestamp
    std::map<int, std::vector<Log>> logs_;

    void removeExpiredLogs() {
        int cutoff = latestTimestamp_ - 3600;

        // Requirement says logs must be received less than one hour ago,
        // so timestamp <= cutoff is expired.
        while (!logs_.empty() && logs_.begin()->first <= cutoff) {
            logCount_ -= static_cast<int>(logs_.begin()->second.size());
            logs_.erase(logs_.begin());
        }
    }

public:
    explicit LogServer(int m) : maxLogs_(m) {}

    void recordLog(const std::string& logId, int timestamp) {
        if (timestamp > latestTimestamp_) {
            latestTimestamp_ = timestamp;
            removeExpiredLogs();
        }

        // An out-of-order log may already be outside the active window.
        if (timestamp <= latestTimestamp_ - 3600) {
            return;
        }

        logs_[timestamp].push_back({logId, nextSequence_++});
        ++logCount_;
    }

    std::string getLogs() const {
        std::vector<std::string> result;
        result.reserve(std::min(maxLogs_, logCount_));

        // Traverse backward because we need the latest m logs.
        for (auto timestampIt = logs_.rbegin();
             timestampIt != logs_.rend() &&
             static_cast<int>(result.size()) < maxLogs_;
             ++timestampIt) {

            const auto& logsAtTimestamp = timestampIt->second;

            // Later-received logs at the same timestamp are considered later.
            for (auto logIt = logsAtTimestamp.rbegin();
                 logIt != logsAtTimestamp.rend() &&
                 static_cast<int>(result.size()) < maxLogs_;
                 ++logIt) {
                result.push_back(logIt->logId);
            }
        }

        // We collected latest -> earliest, but output must be ascending.
        std::reverse(result.begin(), result.end());

        std::ostringstream output;
        for (std::size_t i = 0; i < result.size(); ++i) {
            if (i > 0) {
                output << ',';
            }
            output << result[i];
        }

        return output.str();
    }

    int getLogCount() const {
        return logCount_;
    }
};

// 如果有几千万条日志怎么办？std::deque<{timestamp, std::vector<Log>}> 因为删除永远发生在最前面
// 乱序？std::map 
// 如果多线程读远大于写：recordLog() unique_lock getLogs() shared_lock
// 为什么不用 priority_queue
