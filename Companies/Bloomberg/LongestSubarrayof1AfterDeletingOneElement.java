
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

public int longestSubarray(int[] nums) {
        int len = nums.length;
        int[] left = new int[len+1];
        int[] right = new int[len+1];
        for (int i = 1; i <= len; i++) {
            if (nums[i-1] == 1) {
                left[i] = left[i-1]+1;
            }
        }
        for (int i = len-2; i >= 0; i--) {
            if (nums[i+1] == 1) {
                right[i] = right[i+1]+1;
            }
        }
        int max = 0;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, left[i]+right[i]);
        }
        return max;
    }
