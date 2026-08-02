// Example 1:

// Input: nums = [1,3,-1,-3,5,3,6,7], k = 3
// Output: [3,3,5,5,6,7]
// Explanation: 
// Window position                Max
// ---------------               -----
// [1  3  -1] -3  5  3  6  7       3
//  1 [3  -1  -3] 5  3  6  7       3
//  1  3 [-1  -3  5] 3  6  7       5
//  1  3  -1 [-3  5  3] 6  7       5
//  1  3  -1  -3 [5  3  6] 7       6
//  1  3  -1  -3  5 [3  6  7]      7


// 同时维护最大值和最小值？maxDeque / minDeque
class Solution {
public:
    vector<int> maxSlidingWindow(vector<int>& nums, int k) {
            vector<int> re{};
            deque<int> dp{};
            for (int i = 0; i < nums.size(); i++)
            {
                if (!dp.empty() && dp.front() < i - k + 1)
                {
                    dp.pop_front();
                }
                while (!dp.empty() && nums[dp.back()] < nums[i])
                {
                    dp.pop_back();
                }
                dp.push_back(i);
                if (i < k - 1)
                    continue;
                re.push_back(nums[dp.front()]);
            }
            return re;
    }
};

// Median_Data_Stream.cpp  
// MedianFinder 用两个 priority_queue，为什么 Sliding Window Median 不用？ 
// MedianFinder 没有删除操作，而 Sliding Window 有。
class Solution { 
public:
    vector<double> medianSlidingWindow(vector<int>& nums, int k) {
        vector<double> result;

        for (int i = 0; i < nums.size(); ++i) {
            add(nums[i]);
            if (i >= k) {
                remove(nums[i - k]);
            }

            if (i >= k - 1) {
                result.push_back(getMedian(k));
            }
        }
        return result;
    }

private:
    multiset<int> left;   // 较小的一半
    multiset<int> right;  // 较大的一半

    void rebalance() {
        // left 最多比 right 多一个
        if (left.size() > right.size() + 1) {
            auto it = prev(left.end()); // left 最大值
            right.insert(*it);
            left.erase(it);
        }

        if (left.size() < right.size()) {
            auto it = right.begin(); // right 最小值
            left.insert(*it);
            right.erase(it);
        }
    }

    void add(int value) {
        if (left.empty() || value <= *prev(left.end())) {
            left.insert(value);
        } else {
            right.insert(value);
        }

        rebalance();
    }

    void remove(int value) {
        auto it = left.find(value);

        if (it != left.end()) {
            left.erase(it);
        } else {
            it = right.find(value);
            if (it != right.end()) {
                right.erase(it);
            }
        }

        rebalance();
    }

    double getMedian(int k) const {
        if (k % 2 == 1) {
            return static_cast<double>(*prev(left.end()));
        }

        long long a = *prev(left.end());
        long long b = *right.begin();

        return (a + b) / 2.0;
    }
};



class Solution {
public:
    vector<vector<int>> slidingWindowTopK(const vector<int>& nums, int windowSize, int k) {
        multiset<int> topK; // 最大的 k 个元素
        multiset<int> rest;
        vector<vector<int>> result;

        auto rebalance = [&]() {
            // 保证 topK 最多有 k 个元素
            while (static_cast<int>(topK.size()) > k) {
                auto it = topK.begin(); // topK 中最小的
                rest.insert(*it);
                topK.erase(it);
            }

            // 保证 topK 有 k 个元素
            while (static_cast<int>(topK.size()) < k && !rest.empty()) {
                auto it = prev(rest.end()); // rest 中最大的
                topK.insert(*it);
                rest.erase(it);
            }

            // 保证 topK 中的元素都 >= rest 中的元素
            while (!topK.empty() && !rest.empty()) {
                auto smallestTop = topK.begin();
                auto largestRest = prev(rest.end());

                if (*smallestTop >= *largestRest) {
                    break;
                }

                int topValue = *smallestTop;
                int restValue = *largestRest;

                topK.erase(smallestTop);
                rest.erase(largestRest);

                topK.insert(restValue);
                rest.insert(topValue);
            }
        };

        auto add = [&](int value) {
            if (topK.empty() || value >= *topK.begin()) {
                topK.insert(value);
            } else {
                rest.insert(value);
            }

            rebalance();
        };

        auto remove = [&](int value) {
            auto it = topK.find(value);

            if (it != topK.end()) {
                topK.erase(it);
            } else {
                it = rest.find(value);

                if (it != rest.end()) {
                    rest.erase(it);
                }
            }

            rebalance();
        };

        for (int right = 0; right < static_cast<int>(nums.size()); ++right) {
            add(nums[right]);

            if (right >= windowSize) {
                remove(nums[right - windowSize]);
            }

            if (right >= windowSize - 1) {
                // multiset 默认升序，所以反向遍历得到降序 Top K
                result.emplace_back(topK.rbegin(), topK.rend());
            }
        }

        return result;
    }
};
