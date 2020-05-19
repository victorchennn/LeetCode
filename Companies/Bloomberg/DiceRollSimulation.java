package Companies.Bloomberg;

public class DiceRollSimulation {
    public int dieSimulator(int n, int[] rollMax) {
        int mod = (int)1e9+7;
        int[][] dp = new int[n+1][7];
        for (int i = 0; i < 6; i++) {
            dp[1][i] = 1;
        }
        dp[1][6] = 6;
        for (int i = 2; i <= n; i++) {
            int sum = 0;
            for (int j = 0; j < 6; j++) {
                dp[i][j] = dp[i-1][6];
                if (i - rollMax[j] == 1) {
                    dp[i][j]--;
                } else if (i - rollMax[j] >= 2) { // i = 4, rollMax = 2, suppose number is 6
                    int last = i-rollMax[j]-1;  // first level
                    int reduc = dp[last][6]-dp[last][j];
                    // we use last level sum, which already has not include '666', we minus first level sum,
                    // 1666, 2666, ... 6666, but add 1, which is 6666, since we already has not include 666*
                    // case in the last level sum, so first three digits can't be 666.
                    dp[i][j] = ((dp[i][j]-reduc)%mod+mod)%mod;
                }
                sum = (sum + dp[i][j])%mod;
            }
            dp[i][6] = sum;
        }
        return dp[n][6];
    }
}
