// Given a string s, rearrange the characters of s so that any two adjacent characters are not the same.
// Return any possible rearrangement of s or return "" if not possible.
// Input: s = "aab"
// Output: "aba"

class Solution {
public:
    string reorganizeString(string s) {
        vector<int> count(26, 0);
        for (char c : s) {
            ++count[c - 'a'];
        }

        priority_queue<pair<int, int>> pq; // {出现次数, 字符下标}
        for (int i = 0; i < 26; ++i) {
            if (count[i] > 0) {
                pq.push({count[i], i});
            }
        }

        if (pq.top().first > (s.size() + 1) / 2) {
            return "";
        }

        string result;
        pair<int, int> prev{0, 0};
        while (!pq.empty()) {
            auto current = pq.top();
            pq.pop();

            if (prev.first > 0) { // 上一次使用的字符现在才放回去，保证不会连续选择同一个字符
                pq.push(prev);
            }

            result.push_back(static_cast<char>('a' + current.second));

            --current.first;
            prev = current;
        }

        return result;
    }
};

// 相同字符之间至少相隔 k 个位置。 Task_Scheduler_Idle.cpp 
class Solution {
public:
    string rearrangeString(string s, int k) {
        if (k <= 1) {
            return s;
        }

        unordered_map<char, int> freq;
        for (char c : s) {
            ++freq[c];
        }

        priority_queue<pair<int, char>> maxHeap; // {剩余次数, 字符}
        for (const auto& [c, count] : freq) {
            maxHeap.push({count, c});
        }

        queue<pair<int, char>> cooling; // {剩余次数, 字符}
        string result;
        while (!maxHeap.empty()) {
            auto [count, c] = maxHeap.top();
            maxHeap.pop();

            result.push_back(c);
            --count;

            cooling.push({count, c}); // 当前字符进入冷却队列

            // 队列里已经有 k 个最近使用的字符，
            // 最早使用的字符现在可以重新进入 heap
            if (cooling.size() >= k) {
                auto [oldCount, oldChar] = cooling.front();
                cooling.pop();

                if (oldCount > 0) {
                    maxHeap.push({oldCount, oldChar});
                }
            }
        }

        return result.size() == s.size() ? result : "";
    }

// 要输出所有不含相邻相同字符的排列
class Solution {
public:
    vector<string> reorganizeStringAll(const string& s) {
        vector<int> count(26, 0);

        for (char c : s) {
            ++count[c - 'a'];
        }

        vector<string> result;
        string path;

        backtrack(count, static_cast<int>(s.size()), -1, path, result);
        return result;
    }

private:
    void backtrack(vector<int>& count,
                   int targetLength,
                   int previous,
                   string& path,
                   vector<string>& result) {
        if (path.size() == targetLength) {
            result.push_back(path);
            return;
        }

        for (int current = 0; current < 26; ++current) {
            // 当前字符已经用完
            if (count[current] == 0) {
                continue;
            }

            // 不能和上一个字符相同
            if (current == previous) {
                continue;
            }

            // 选择
            --count[current];
            path.push_back(static_cast<char>('a' + current));

            backtrack(count, targetLength, current, path, result);

            // 撤销选择
            path.pop_back();
            ++count[current];
        }
    }
};
};
