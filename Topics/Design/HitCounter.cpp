// You need to design a hit counter system that tracks the number of hits received within the past 5 minutes (300 seconds).

// Recording hits: When a hit occurs at a specific timestamp (in seconds), the system should record it. 
// Multiple hits can happen at the same timestamp.

// Querying hit count: Given a timestamp, the system should return the total number of hits that occurred in the past 300 seconds from that timestamp. 
// Specifically, it counts all hits in the time range [timestamp - 299, timestamp].

class HitCounter {
public:
    HitCounter() : totalHits(0) {}

    void hit(int timestamp) {
        // lock_guard<mutex> lock(mutex_);
        
        if (hits.empty() || hits.back().first != timestamp) {
            hits.push_back({timestamp, 1});
        } else {
            ++hits.back().second;
        }

        ++totalHits;
    }

    int getHits(int timestamp) {
        // lock_guard<mutex> lock(mutex_);
        
        while (!hits.empty() &&
               hits.front().first + 300 <= timestamp) {
            totalHits -= hits.front().second;
            hits.pop_front();
        }

        return totalHits;
    }

private:
    deque<pair<int, int>> hits; // {timestamp, count}
    int totalHits;
    // mutex mutex_;
};
