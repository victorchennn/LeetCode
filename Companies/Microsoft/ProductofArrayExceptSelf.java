package Companies.Microsoft;

public class ProductofArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int[] dp = new int[nums.length];
        dp[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            dp[i] = dp[i-1]*nums[i-1];
        }
        int num = 1;
        for (int i = nums.length-2; i >= 0; i--) {
            num *= nums[i+1];
            dp[i] *= num;
        }
        return dp;
    }
}
