package Companies.ByteDance;

public class MaximumProductSubarray {
    public int maxProduct(int[] nums) {
        if (nums.length == 0) {
            return 0;
        }
        int min = nums[0], max = nums[0], re = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int temp = max;
            max = Math.max(nums[i], Math.max(max*nums[i], min*nums[i]));
            min = Math.min(nums[i], Math.min(temp*nums[i], min*nums[i]));
            if (max > re) {
                re = max;
            }
        }
        return re;
    }
}
