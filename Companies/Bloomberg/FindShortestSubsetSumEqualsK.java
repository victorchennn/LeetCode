package Companies.Bloomberg;

import java.util.Arrays;

/**
 * Coin Change problem
 */
public class FindShortestSubsetSumEqualsK {
    public static int find(int[] nums, int K) {
        if (K == 0) {
            return 0;
        }
        int[] dp = new int[K+1];
        Arrays.fill(dp, nums.length+1);
        dp[0] = 0;
        for (int i = 1; i <= K; i++) {
            for (int num : nums) {
                if (num > i) {
                    continue;
                }
                dp[i] = Math.min(dp[i], dp[i-num]+1);
            }
        }
        return dp[K] == (nums.length+1)? -1:dp[K];
    }

    public static void main(String...args) {
        System.out.println(find(new int[]{5,3,2,7,10,15}, 10));
        System.out.println(find(new int[]{5,3,2,7,10,15}, 5));
        System.out.println(find(new int[]{1,5,3,2,7,10,15}, 8));
    }
}
