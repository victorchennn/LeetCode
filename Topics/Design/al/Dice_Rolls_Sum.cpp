// You have n dice, and each die has k faces numbered from 1 to k.
// Given three integers n, k, and target, you need to find the number of possible ways to roll all the dice such that the sum of the face-up numbers equals exactly target.
// 2 dice with 6 faces each and want a sum of 7, 
// the valid combinations would be: (1,6), (2,5), (3,4), (4,3), (5,2), (6,1), giving us 6 possible ways.

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
