package Companies.Microsoft;

import java.util.Arrays;

public class UniquePaths {
    public int uniquePaths(int m, int n) {
        int[] step = new int[n];
        Arrays.fill(step, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                step[j] += step[j-1];
            }
        }
        return step[n-1];
    }

    /* Obstacles */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[][] dp = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[i][j] = 0;
                } else if (i == 0 || j == 0) {
                    if (i == 0 && j == 0) {
                        dp[i][j] = 1;
                    } else {
                        dp[i][j] = i == 0?dp[i][j-1]:dp[i-1][j];
                    }
                } else {
                    dp[i][j] = dp[i-1][j] + dp[i][j-1];
                }
            }
        }
        return dp[m-1][n-1];
    }
}
