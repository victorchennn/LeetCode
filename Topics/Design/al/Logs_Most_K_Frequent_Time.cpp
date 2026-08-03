// op k element，还挺practical的
// 给一个list of logs， 每个element有个timestamp， 一个error code， find most frequent k error code between start time and end time

struct Log {
    long long timestamp;
    int errorCode;
};

vector<int> topKErrorCode(const vector<Log>& logs, long long start, long long end, int k) {
    unordered_map<int, int> freq;
    for (const auto& log : logs) {
        if (log.timestamp >= start &&
            log.timestamp <= end) {
            ++freq[log.errorCode];
        }
    }

    using Pair = pair<int, int>; // (count, errorCode)
    priority_queue<Pair, vector<Pair>, greater<Pair>> minHeap;

    for (auto& [code, count] : freq) {
        minHeap.push({count, code});

        if (minHeap.size() > k) {
            minHeap.pop();
        }
    }

    vector<int> result;

    while (!minHeap.empty()) {
        result.push_back(minHeap.top().second);
        minHeap.pop();
    }

    reverse(result.begin(), result.end());
    return result;
}

// max heap? 所有频率入堆，弹出 k 次。
