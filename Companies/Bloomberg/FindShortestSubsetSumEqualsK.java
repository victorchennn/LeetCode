package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see CoinChange
 */
public class FindShortestSubsetSumEqualsK {
    public int find(int[] nums, int K) {
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

    @Test
    void test() {
        assertEquals(1, find(new int[]{5,3,2,7,10,15}, 10));
        assertEquals(1, find(new int[]{5,3,2,7,10,15}, 5));
        assertEquals(2, find(new int[]{1,5,3,2,7,10,15}, 8));
    }
}
