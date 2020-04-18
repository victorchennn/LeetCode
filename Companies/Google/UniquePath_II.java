package Companies.Google;

public class UniquePath_II {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        for (int i = m-1; i >= 0; i--) {
            if (obstacleGrid[i][n-1] != 1) {
                dp[i][n-1] = 1;
            } else {
                break;
            }
        }
        for (int i = n-1; i >= 0; i--) {
            if (obstacleGrid[m-1][i] != 1) {
                dp[m-1][i] = 1;
            } else {
                break;
            }
        }
        for (int i = m-2; i >= 0; i--) {
            for (int j = n-2; j >= 0; j--) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else {
                    dp[i][j] = dp[i+1][j] + dp[i][j+1];
                }
            }
        }
        return dp[0][0];
    }
}
