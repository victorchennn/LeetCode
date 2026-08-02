// change nums[i] to nums[i] + x where x is an integer from the range [-k, k]. 
// The score of nums is the difference between the maximum and minimum elements in nums.
// Return the minimum score of nums after applying the mentioned operation at most once for each index in it.

class Solution {
public:
    int smallestRangeI(vector<int>& nums, int k) {
        auto [minIter, maxIter] = minmax_element(nums.begin(), nums.end());
      
        // Calculate the smallest possible range after applying operations
        // We can add any value in [-k, k] to each element
        // To minimize range: add k to min element, subtract k from max element
        // New range = (max - k) - (min + k) = max - min - 2k
        // If this value is negative, the range can be reduced to 0
        return max(0, *maxIter - *minIter - 2 * k);
    }
};

// change nums[i] to be either nums[i] + k or nums[i] - k.
// The score of nums is the difference between the maximum and minimum elements in nums.
// Return the minimum score of nums after changing the values at each index.

class Solution {
public:
    int smallestRangeII(vector<int>& nums, int k) {
        sort(nums.begin(), nums.end());
        int n = nums.size();
      
        int answer = nums[n - 1] - nums[0];
      
        for (int i = 1; i < n; ++i) {
            int minValue = min(nums[0] + k, nums[i] - k);
            int maxValue = max(nums[i - 1] + k, nums[n - 1] - k);
          
            answer = min(answer, maxValue - minValue);
        }
        return answer;
    }
};

// 返回具体修改后的数组？可以记录最优分界点：int bestSplit = 0;
// if (high - low < answer) {
//     answer = high - low;
//     bestSplit = i;
// }
// for (int i = 0; i < bestSplit; ++i) {
//     nums[i] += k;
// }
// for (int i = bestSplit; i < n; ++i) {
//     nums[i] -= k;
// }

// 保持原数组顺序怎么办？存 (value, index)
// vector<pair<int, int>> arr;
// for (int i = 0; i < n; ++i) {
//     arr.push_back({nums[i], i});
// }
// ...
// result[originalIndex] = (i < bestSplit) ? value + k : value - k;
