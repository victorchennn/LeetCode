// Input: nums = [1,2,3,1,2,3,1,2], k = 2
// Output: 6
// Input: nums = [1,2,1,2,1,2,1,2], k = 1
// Output: 2
    
class Solution {
public:
    int maxSubarrayLength(vector<int>& nums, int k) {
        // Hash map to store the frequency count of each element in current window
        unordered_map<int, int> frequencyMap;
      
        // Variable to track the maximum length of valid subarray
        int maxLength = 0;
      
        // Sliding window approach with left pointer (left) and right pointer (right)
        int left = 0;
      
        for (int right = 0; right < nums.size(); ++right) {
            // Add current element to the window and increment its frequency
            frequencyMap[nums[right]]++;
          
            // Shrink window from left while current element's frequency exceeds k
            while (frequencyMap[nums[right]] > k) {
                // Remove leftmost element from window and decrement its frequency
                frequencyMap[nums[left]]--;
                // Move left pointer to the right
                left++;
            }
          
            // Update maximum length with current valid window size
            maxLength = max(maxLength, right - left + 1);
        }
      
        return maxLength;
    }
};
