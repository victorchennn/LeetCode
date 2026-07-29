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
