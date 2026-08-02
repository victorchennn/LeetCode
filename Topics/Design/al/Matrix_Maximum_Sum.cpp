// 给一个n*n matrix，从每一行和每一列取且仅取一个数求和，最大的和是多少？ 
// Example: matrix = [[1,2,3], [3,4,6], [3,2,3]] max_sum = sum([2,6,3])=11

class Solution {
public:
    int maxSum(vector<vector<int>>& matrix) {
        int n = matrix.size();
        vector<bool> used(n, false);
        int ans = INT_MIN;

        dfs(matrix, 0, used, 0, ans);
        return ans;
    }

private:
    void dfs(vector<vector<int>>& matrix, int row, vector<bool>& used, int sum, int& ans) {
        int n = matrix.size();
        if (row == n) {
            ans = max(ans, sum);
            return;
        }

        for (int col = 0; col < n; col++) {
            if (used[col]) continue;

            used[col] = true;
            dfs(matrix, row + 1, used, sum + matrix[row][col], ans);
            used[col] = false;
        }
    }
};

// Bitmask DP
class Solution {
public:
    int maxSum(vector<vector<int>>& matrix) {
        int n = matrix.size();

        vector<int> dp(1 << n, INT_MIN);
        dp[0] = 0;

        for (int mask = 0; mask < (1 << n); mask++) {
            if (dp[mask] == INT_MIN) continue;

            int row = __builtin_popcount(mask);

            if (row == n) continue;

            for (int col = 0; col < n; col++) {
                if (!(mask & (1 << col))) { // check if col is used? 
                    int next = mask | (1 << col);
                    dp[next] = max(dp[next], dp[mask] + matrix[row][col]);
                }
            }
        }

        return dp[(1 << n) - 1];
    }
};

// Hungarian Algorithm?
