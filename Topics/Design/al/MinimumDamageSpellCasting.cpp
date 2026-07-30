#include <algorithm>
#include <unordered_map>
#include <vector>

using namespace std;

class Solution {
public:
    long long maximumTotalDamage(vector<int>& power) {
        unordered_map<int, long long> totalDamage;

        for (int damage : power) {
            totalDamage[damage] += damage;
        }

        vector<int> values;
        values.reserve(totalDamage.size());

        for (const auto& [damage, total] : totalDamage) {
            values.push_back(damage);
        }

        sort(values.begin(), values.end());

        int n = values.size();
        vector<long long> dp(n);

        for (int i = 0; i < n; ++i) {
            long long take = totalDamage[values[i]];

            // 找到最后一个 <= values[i] - 3 的伤害值
            int j = upper_bound(
                        values.begin(),
                        values.begin() + i,
                        values[i] - 3
                    ) - values.begin() - 1;

            if (j >= 0) {
                take += dp[j];
            }

            long long skip = (i > 0) ? dp[i - 1] : 0;

            dp[i] = max(skip, take);
        }

        return dp[n - 1];
    }
};
