package Topics.SlidingWindow;

public class MinimumSizeSubarraySum {
    public int minSubArrayLen(int s, int[] nums) {
        int l = 0, r = 0, sum = 0, re = Integer.MAX_VALUE;
        while (r < nums.length) {
            sum += nums[r];
            while (sum >= s) {
                if (r-l+1 < re) {
                    re = r-l+1;
                }
                sum -= nums[l];
                l++;
            }
            r++;
        }
        return re == Integer.MAX_VALUE?0:re;
    }
}
