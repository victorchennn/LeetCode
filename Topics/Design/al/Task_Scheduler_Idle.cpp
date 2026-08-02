// Input: tasks = ["A","A","A","B","B","B"], n = 2
// Output: 8
  
// Explanation: A possible sequence is: A -> B -> idle -> A -> B -> idle -> A -> B.

int leastInterval(vector<char>& tasks, int n) {
    unordered_map<char, int> count;

    int maxFreq = 0;
    for (char task : tasks) {
        maxFreq = max(maxFreq, ++count[task]);
    }

    int maxFreqTaskCount = 0;
    for (const auto& [task, freq] : count) {
        if (freq == maxFreq) {
            ++maxFreqTaskCount;
        }
    }
    // A _ _ A _ _ A 
    // (A _ _ A _ _ == (maxFreq - 1) * (n + 1))
    int frame = (maxFreq - 1) * (n + 1) + maxFreqTaskCount;
    return max(static_cast<int>(tasks.size()), frame);
}

Follow-up：输出具体调度顺序，而不仅仅是长度
// priority_queue<pair<int, char>>：按剩余次数排序。
// queue<pair<int, pair<int, char>>>：冷却队列 (readyTime, (count, task))

vector<string> scheduleTasks(vector<char>& tasks, int n) {
    unordered_map<char, int> freq;
    for (char task : tasks) {
        ++freq[task];
    }

    // {剩余次数, task}
    priority_queue<pair<int, char>> available;
    for (const auto& [task, count] : freq) {
        available.push({count, task});
    }

    struct CoolingTask {
        int readyTime;
        int remaining;
        char task;
    };

    queue<CoolingTask> cooling;
    vector<string> schedule;

    int time = 0;

    while (!available.empty() || !cooling.empty()) {
        // 当前时间已经冷却完成的任务重新放回堆
        while (!cooling.empty() && cooling.front().readyTime <= time) {
            auto current = cooling.front();
            cooling.pop();

            available.push({
                current.remaining,
                current.task
            });
        }

        if (available.empty()) {
            schedule.push_back("idle");
        } else {
            auto [remaining, task] = available.top();
            available.pop();

            schedule.push_back(string(1, task)); // 创建一个长度为1的字符串，这个字符是task。
            --remaining;

            if (remaining > 0) {
                cooling.push({
                    time + n + 1,
                    remaining,
                    task
                });
            }
        }

        ++time;
    }

    return schedule;
}
