package Companies.ByteDance;

public class MaximumSubarray {
    public int maxSubArray(int[] nums) {
        int max = nums[0];
        for (int i = 1; i < nums.length; i++) {
            if (nums[i-1] > 0) {
                nums[i] += nums[i-1];
            }
            max = Math.max(max, nums[i]);
        }
        return max;
    }

    public int maxSubArray_II(int[] nums) {
        int max = Integer.MIN_VALUE, sum = 0;
        for (int num : nums) {
            sum = Math.max(sum+num, num);
            max = Math.max(max, sum);
        }
        return max;
    }
}
