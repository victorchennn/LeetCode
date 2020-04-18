package Companies.GoldmanSachs;

public class MinimumSizeSubarraySum {
    public int minSubArrayLen(int s, int[] nums) {
        int l = 0, sum = 0, re = Integer.MAX_VALUE;
        for (int r = 0; r < nums.length; r++) {
            sum += nums[r];
            while (sum >= s) {
                re = Math.min(re, r-l+1);
                sum -= nums[l];
                l++;
            }
        }
        return re == Integer.MAX_VALUE? 0:re;
    }
}
