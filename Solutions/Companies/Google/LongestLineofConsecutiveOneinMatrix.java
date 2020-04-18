package Companies.Google;

public class LongestLineofConsecutiveOneinMatrix {
    public int longestLine(int[][] M) {
        if (M.length == 0 || M[0].length == 0) {
            return 0;
        }
        int m = M.length, n = M[0].length, re = 0;
        int[][][] dp = new int[m][n][4];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (M[i][j] == 0) {
                    continue;
                }
                for (int l = 0; l < 4; l++) {
                    dp[i][j][l] = 1;
                }
                if (i > 0) {
                    dp[i][j][0] += dp[i-1][j][0];
                }
                if (j > 0) {
                    dp[i][j][1] += dp[i][j-1][1];
                }
                if (i > 0 && j > 0) {
                    dp[i][j][2] += dp[i-1][j-1][2];
                }
                if (i > 0 && j < n-1) {
                    dp[i][j][3] += dp[i-1][j+1][3];
                }
                re = Math.max(re, Math.max(dp[i][j][0], dp[i][j][1]));
                re = Math.max(re, Math.max(dp[i][j][2], dp[i][j][3]));
            }
        }
        return re;
    }
}
