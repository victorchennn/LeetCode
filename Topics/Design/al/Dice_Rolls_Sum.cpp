class Solution {
public:
    int numRollsToTarget(int d, int f, int target) {
        static constexpr int MOD = 1'000'000'007;

        vector<vector<int>> dp(d + 1, vector<int>(target + 1, 0));
        dp[0][0] = 1;
        for (int dice = 1; dice <= d; ++dice) {
            for (int sum = 1; sum <= target; ++sum) {
                if (sum > dice * f) {
                    break;
                }
                for (int face = 1; face <= f && face <= sum; ++face) {
                    dp[dice][sum] = (dp[dice][sum] + dp[dice - 1][sum - face]) % MOD;
                }
            }
        }

        return dp[d][target];
    }
};
