package Companies.Microsoft;

public class MaximumSubarrayProduct {
    public int maxProduct(int[] nums) {
        int min = nums[0], max = nums[0], re = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int temp = min;
            min = Math.min(nums[i], Math.min(min*nums[i], max*nums[i]));
            max = Math.max(nums[i], Math.max(max*nums[i], temp*nums[i]));
            re = Math.max(re, max);
        }
        return re;
    }
}
