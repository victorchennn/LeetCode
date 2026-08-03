class Solution {
public:
    int optimalStrategy(const vector<int>& arr) {
        int n = arr.size();
        if (n == 0) {
            return 0;
        }

        vector<vector<int>> dp(n, vector<int>(n, 0)); // dp[l][r] 表示当前玩家面对区间 [l, r] 时，最终能够保证获得的最大金额。

        // 只有一个硬币时，当前玩家直接拿走
        for (int i = 0; i < n; ++i) {
            dp[i][i] = arr[i];
        }

        // 两个硬币时，选择较大的
        for (int i = 0; i + 1 < n; ++i) {
            dp[i][i + 1] = max(arr[i], arr[i + 1]);
        }

        // 区间长度从 3 开始
        for (int len = 3; len <= n; ++len) {
            for (int l = 0; l + len - 1 < n; ++l) {
                int r = l + len - 1;

                // 拿走 arr[l] 后，对手面对 [l + 1, r] 
                // 对手可能拿：arr[l + 1]，剩下 [l + 2, r]
                              arr[r]，剩下 [l + 1, r - 1]
                // 所以我们之后最多只能保证：min(dp[l + 2][r], dp[l + 1][r - 1])

                int afterLeftLeft = (l + 2 <= r) ? dp[l + 2][r] : 0;
                int afterLeftRight = (l + 1 <= r - 1) ? dp[l + 1][r - 1] : 0;
                int afterRightRight =  (l <= r - 2) ? dp[l][r - 2] : 0;

                int takeLeft = arr[l] + min(afterLeftLeft, afterLeftRight);
                int takeRight = arr[r] + min(afterLeftRight, afterRightRight);

                dp[l][r] = max(takeLeft, takeRight);
            }
        }

        return dp[0][n - 1];
    }
};

int main() {
    Solution solution;

    cout << solution.optimalStrategy({5, 3, 7, 10}) << '\n'; // 15
    cout << solution.optimalStrategy({8, 15, 3, 7}) << '\n'; // 22
}
