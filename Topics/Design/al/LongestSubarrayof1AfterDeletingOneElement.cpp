
// Example 1:
// Input: nums = [1,1,0,1]
// Output: 3
// Explanation: After deleting the number in position 2, [1,1,1] contains 3 numbers with value of 1's.

// Example 2:
// Input: nums = [0,1,1,1,0,1,1,0,1]
// Output: 5
// Explanation: After deleting the number in position 4, [0,1,1,1,1,1,0,1] longest subarray with value of 1's is [1,1,1,1,1].

// Example 3:
// Input: nums = [1,1,1]
// Output: 2
// Explanation: You must delete one element.

public:
    int longestSubarray(const std::vector<int>& nums) {
        int left = 0;
        int zeroCount = 0;
        int result = 0;
    
        for (int right = 0; right < static_cast<int>(nums.size()); ++right) {
            if (nums[right] == 0) {
                ++zeroCount;
            }
    
            while (zeroCount > 1) {
                if (nums[left] == 0) {
                    --zeroCount;
                }
                ++left;
            }

            //   int windowLength = right - left + 1;
            //   int onesAfterDeletion = windowLength - (zeroCount == 1 ? 1 : 0);
            //   result = std::max(result, onesAfterDeletion);
    
            // Window length is right - left + 1,
            // but one element must be deleted.
            result = std::max(result, right - left);
        }
    
        return result;
    }

    int longestOnes(const std::vector<int>& nums, int k) {
        int left = 0;
        int zeroCount = 0;
        int result = 0;
    
        for (int right = 0; right < nums.size(); ++right) {
            if (nums[right] == 0) {
                ++zeroCount;
            }
    
            while (zeroCount > k) {
                if (nums[left] == 0) {
                    --zeroCount;
                }
                ++left;
            }
    
            result = std::max(result, right - left + 1);
        }
    
        return result;
    }

    int longestSubarray(const std::vector<int>& nums) {
        int n = static_cast<int>(nums.size());

        std::vector<int> left(n + 1, 0);
        std::vector<int> right(n + 1, 0);

        // left[i] = number of consecutive 1s immediately before index i.
        for (int i = 1; i <= n; ++i) {
            if (nums[i - 1] == 1) {
                left[i] = left[i - 1] + 1;
            }
        }

        // right[i] = number of consecutive 1s immediately after index i.
        for (int i = n - 2; i >= 0; --i) {
            if (nums[i + 1] == 1) {
                right[i] = right[i + 1] + 1;
            }
        }

        int result = 0;

        // Delete nums[i], then connect the 1s on both sides.
        for (int i = 0; i < n; ++i) {
            result = std::max(result, left[i] + right[i]);
        }

        return result;
    }

};
