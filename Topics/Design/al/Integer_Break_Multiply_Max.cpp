// 给定长度 n，必须至少切一刀，把它拆成若干个正整数，使乘积最大。
// n = 10
// 3 + 3 + 4
// 最大乘积 = 3 × 3 × 4 = 36

class Solution {
public:
    int integerBreak(int n) {
        return dfs(n);
    }

private: // 暴力递归会重复计算大量相同状态，时间复杂度接近指数级。
    int dfs(int n) {
        int result = 0;
        for (int i = 1; i < n; ++i) {
            result = max(result, max(i * (n - i), i * dfs(n - i)));
        }
        return result;
    }
};

// Memoization 时间O(n²) 空间O(n)
class Solution {
public:
    int integerBreak(int n) {
        vector<int> memo(n + 1, -1);
        return dfs(n, memo);
    }

private:
    int dfs(int n, vector<int>& memo) {
        if (memo[n] != -1) {
            return memo[n];
        }

        int result = 0;
        for (int i = 1; i < n; ++i) {
            result = max(result, max(i * (n - i), i * dfs(n - i, memo)));
        }
        return memo[n] = result;
    }
};

// Bottom-up DP 时间O(n²) 空间O(n)
class Solution {
public:
    int integerBreak(int n) {
        vector<int> dp(n + 1, 0);

        for (int len = 2; len <= n; ++len) {
            for (int i = 1; i < len; ++i) {
                dp[len] = max(dp[len],
                              max(i * (len - i),
                                  i * dp[len - i]));
            }
        }

        return dp[n];
    }
};
