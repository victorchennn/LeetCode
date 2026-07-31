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
