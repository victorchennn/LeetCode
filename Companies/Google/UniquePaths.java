package Companies.Google;

import java.util.Arrays;

public class UniquePaths {
    public int uniquePaths(int m, int n) {
        int[] dp = new int[n];
        Arrays.fill(dp, 1);
        for (int i = 1; i < m; i++) {
            for (int j = 1; j < n; j++) {
                dp[j] += dp[j-1];
            }
        }
        return dp[n-1];
    }

    /* Obstacles */
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        int m = obstacleGrid.length, n = obstacleGrid[0].length;
        int[] dp = new int[n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (obstacleGrid[i][j] == 1) {
                    dp[j] = 0;
                } else if (j == 0) {
                    if (i == 0) {
                        dp[j] = 1;
                    } else {
                        continue;
                    }
                } else {
                    dp[j] += dp[j-1];
                }
            }
        }
        return dp[n-1];
    }

    private int sx, sy, ex, ey, aval = 0, re = 0;
    private int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public int uniquePathsIII(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] >= 0) {
                    aval += 1;
                    if (grid[i][j] == 1) {
                        sx = i;
                        sy = j;
                    } else if (grid[i][j] == 2) {
                        ex = i;
                        ey = j;
                    }
                }
            }
        }
        dfs(grid, sx, sy, aval);
        return re;
    }

    private void dfs(int[][] grid, int x, int y, int aval) {
        if (x == ex && y == ey) {
            if (aval == 1) {
                re++;
                return;
            }
        }
        grid[x][y] = -2;
        for (int[] dir : dirs) {
            int xx = x + dir[0];
            int yy = y + dir[1];
            if (xx >= 0 && xx < grid.length && yy >= 0 && yy < grid[0].length && grid[xx][yy] >= 0) {
                dfs(grid, xx, yy, aval-1);
            }
        }
        grid[x][y] = 0;
    }
}
