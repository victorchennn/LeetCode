package Companies.Bloomberg;

public class MaximumSubarraySum {
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

    /* Divide and Conquer */
    public int maxSubArrayIII(int[] nums) {
        return helper(nums, 0, nums.length-1);
    }

    private int helper(int[] nums, int l, int r) {
        if (l == r) {
            return nums[l];
        }
        int m = l + (r-l)/2;
        int left = helper(nums, l, m);
        int right = helper(nums, m+1, r);
        int comb = cross(nums, l, r, m);
        return Math.max(Math.max(left, right), comb);
    }

    private int cross(int[] nums, int l, int r, int mid) {
        if (l == r) {
            return nums[l];
        }
        int leftSum = Integer.MIN_VALUE, rightSum = Integer.MIN_VALUE, sum = 0;
        for (int i = mid; i >= l; i--) {
            sum += nums[i];
            leftSum = Math.max(leftSum, sum);
        }
        sum = 0;
        for (int i = mid+1; i <= r; i++) {
            sum += nums[i];
            rightSum = Math.max(rightSum, sum);
        }
        return leftSum+rightSum;
    }
}
