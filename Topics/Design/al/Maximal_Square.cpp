// Given an m x n binary matrix filled with 0's and 1's, 
// find the largest square containing only 1's and return its area.

int maximalSquare2D(const std::vector<std::vector<char>>& matrix) {
    if (matrix.empty() || matrix[0].empty()) {
        return 0;
    }

    int m = matrix.size();
    int n = matrix[0].size();
    int maxSide = 0;

    std::vector<std::vector<int>> dp(
        m + 1,
        std::vector<int>(n + 1, 0)
    );

    for (int i = 1; i <= m; ++i) {
        for (int j = 1; j <= n; ++j) {
            if (matrix[i - 1][j - 1] == '1') {
                dp[i][j] = 1 + std::min({
                    dp[i - 1][j],
                    dp[i][j - 1],
                    dp[i - 1][j - 1]
                });

                maxSide = std::max(maxSide, dp[i][j]);
            }
        }
    }

    return maxSide * maxSide;
}


int maximalSquare(const std::vector<std::vector<char>>& matrix) {
    if (matrix.empty() || matrix[0].empty()) {
        return 0;
    }
  
    int m = matrix.size();
    int n = matrix[0].size();
    int maxSide = 0;
  
    std::vector<int> dp(n + 1, 0);
  
    for (int i = 1; i <= m; ++i) {
        int prev = 0;  // 上一行的 dp[j - 1]，每行必须重置
  
        for (int j = 1; j <= n; ++j) {
            int top = dp[j];
  
            if (matrix[i - 1][j - 1] == '1') {
                dp[j] = 1 + std::min({
                    dp[j],      // 上
                    dp[j - 1],  // 左
                    prev        // 左上
                });
  
                maxSide = std::max(maxSide, dp[j]);
            } else {
                dp[j] = 0;
            }
  
            prev = top;
        }
    }
  
    return maxSide * maxSide;
}
