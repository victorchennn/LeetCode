class Solution {
public:
    int uniquePaths(int m, int n) {
        vector<int> dp(n, 1);
        for (int i = 1; i < m; ++i) {
            for (int j = 1; j < n; ++j) {
                dp[j] += dp[j - 1];
            }
        }
        return dp[n - 1];
    }

    int uniquePathsWithObstacles(vector<vector<int>>& obstacleGrid) {
        int m = obstacleGrid.size();
        int n = obstacleGrid[0].size();
        vector<int> dp(n, 0);

        for (int i = 0; i < m; ++i) {
            for (int j = 0; j < n; ++j) {
                if (obstacleGrid[i][j] == 1) {
                    dp[j] = 0;
                }
                else if (j == 0) {
                    if (i == 0)
                        dp[j] = 1;
                }
                else {
                    dp[j] += dp[j - 1];
                }
            }
        }
        return dp[n - 1];
    }

    int uniquePathsIII(vector<vector<int>>& grid) {
        rows = grid.size();
        cols = grid[0].size();

        available = 0;
        result = 0;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                if (grid[i][j] >= 0) {
                    ++available;
                    if (grid[i][j] == 1) {
                        startX = i;
                        startY = j;
                    }
                    else if (grid[i][j] == 2) {
                        endX = i;
                        endY = j;
                    }
                }
            }
        }

        dfs(grid, startX, startY, available);
        return result;
    }

private:
    int rows, cols;

    int startX, startY;
    int endX, endY;

    int available;
    int result;

    vector<pair<int, int>> dirs{
        {1, 0},
        {-1, 0},
        {0, 1},
        {0, -1}
    };

    void dfs(vector<vector<int>>& grid,
             int x,
             int y,
             int remain) {

        if (x == endX && y == endY) {
            if (remain == 1) {
                ++result;
            }
            return;
        }

        grid[x][y] = -2;      // visited

        for (auto [dx, dy] : dirs) {
            int nx = x + dx;
            int ny = y + dy;

            if (nx >= 0 && nx < rows &&
                ny >= 0 && ny < cols &&
                grid[nx][ny] >= 0) {

                dfs(grid, nx, ny, remain - 1);
            }
        }

        grid[x][y] = 0;       // backtrack
    }
};
