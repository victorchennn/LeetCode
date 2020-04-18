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
                } else if (i - rollMax[j] >= 2) {
                    int reduc = dp[i-rollMax[j]-1][6]-dp[i-rollMax[j]-1][j];
                    dp[i][j] = ((dp[i][j]-reduc)%mod+mod)%mod;
                }
                sum = (sum + dp[i][j])%mod;
            }
            dp[i][6] = sum;
        }
        return dp[n][6];
    }
}
