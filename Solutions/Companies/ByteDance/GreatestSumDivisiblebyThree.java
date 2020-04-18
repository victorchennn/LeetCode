package Companies.ByteDance;

public class GreatestSumDivisiblebyThree {
    public int maxSumDivThree(int[] nums) {
        int[] dp = new int[3];
        for (int num : nums) {
            int[] next = new int[3];
            for (int i = 0; i < 3; i++) {
                next[i] = dp[i];
            }
            for (int sum : dp) {
                int newsum = sum + num;
                int i = newsum % 3;
                next[i] = Math.max(next[i], newsum);
            }
            dp = next;
        }
        return dp[0];
    }
}
