package Companies.VMware;

public class MaximalSquare {
    public int maximalSquare(char[][] matrix) {
        if (matrix.length == 0) {
            return 0;
        }
        int m = matrix.length, n = matrix[0].length, re = 0, prev = 0;
        int[] dp = new int[n+1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int temp = dp[j];
                if (matrix[i-1][j-1] == '1') {
                    dp[j] = 1 + Math.min(dp[j], Math.min(dp[j-1], prev));
                    re = Math.max(dp[j], re);
                } else {
                    dp[j] = 0;
                }
                prev = temp;
            }
        }
        return re*re;
    }

    public int maximalSquareII(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int m = matrix.length, n = matrix[0].length, re = 0;
        int[][] dp = new int[m+1][n+1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (matrix[i-1][j-1] == '1') {
                    dp[i][j] = Math.min(dp[i-1][j-1], Math.min(dp[i][j-1], dp[i-1][j]))+1;
                    re = Math.max(re, dp[i][j]);
                }
            }
        }
        return re*re;
    }
}
