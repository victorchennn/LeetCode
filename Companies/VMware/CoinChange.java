package Companies.VMware;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CoinChange {
    /**
     * fewest number of coins
     * Input: coins = [1, 2, 5], amount = 11
     * Output: 3
     * Explanation: 11 = 5 + 5 + 1
     */
    public int coinChange(int[] coins, int amount) {
        int[] dp = new int[amount+1];
        Arrays.fill(dp, amount+1);
        dp[0] = 0;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] = Math.min(dp[i], dp[i-coin]+1);
            }
        }
        return dp[amount] > amount? -1:dp[amount];
    }


    /**
     * Input: amount = 5, coins = [1, 2, 5]
     * Output: 4
     */
    public static int change(int amount, int[] coins) {
        int[] dp = new int[amount+1];
        dp[0] = 1;
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i-coin];
            }
        }
        return dp[amount];
    }

    @Test
    void test() {
        assertEquals(3, coinChange(new int[]{1,2,5}, 11));
        assertEquals(4, change(5, new int[]{1,2,5}));
        assertEquals(4, change(60, new int[]{10,15,60}));
    }
}
