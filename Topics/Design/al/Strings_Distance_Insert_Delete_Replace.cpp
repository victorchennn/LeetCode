/**
 * Given two words, find the minimum number of operations required to convert word1 to word2.
 * You have the following 3 operations permitted on a word:
 *
 * Insert/Delete/Replace
 */

class Solution {
public:
    int minDistance(string word1, string word2) {
        int m = word1.size();
        int n = word2.size();
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));
        for (int i = 1; i <= m; i++) {
            dp[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            dp[0][j] = j;
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (word1[i - 1] == word2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + min({
                        dp[i - 1][j - 1], // replace
                        dp[i - 1][j], // delete
                        dp[i][j - 1] // insert
                    });
                }
            }
        }

        return dp[m][n];
    }
};

// Follow-up：空间优化到 O(n) 由于每一行只依赖上一行，可以滚动数组
class Solution {
public:
    int minDistance(string word1, string word2) {
        int m = word1.size();
        int n = word2.size();

        vector<int> prev(n + 1), cur(n + 1);
        for (int j = 0; j <= n; j++) {
            prev[j] = j;
        }

        for (int i = 1; i <= m; i++) {
            cur[0] = i;
            for (int j = 1; j <= n; j++) {
                if (word1[i - 1] == word2[j - 1]) {
                    cur[j] = prev[j - 1];
                } else {
                    cur[j] = 1 + min(prev[j - 1], min(prev[j], cur[j - 1]));
                }
            }
            swap(prev, cur);
        }
      
        return prev[n];
    }
};
