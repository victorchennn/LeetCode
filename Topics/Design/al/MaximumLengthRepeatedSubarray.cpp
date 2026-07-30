// Example 1:

// Input: nums1 = [1,2,3,2,1], nums2 = [3,2,1,4,7]
// Output: 3
// Explanation: The repeated subarray with maximum length is [3,2,1].
class Solution {
public:
    int findLength(vector<int>& nums1, vector<int>& nums2) {
        // Get the lengths of both input arrays
        int m = nums1.size();
        int n = nums2.size();

        // Create a 2D DP table where dp[i][j] represents the length of
        // common subarray ending at nums1[i-1] and nums2[j-1]
        // Initialize with zeros (default constructor behavior)
        vector<vector<int>> dp(m + 1, vector<int>(n + 1, 0));

        // Track the maximum length found so far
        int maxLength = 0;

        // Iterate through all possible positions in both arrays
        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                // If elements at current positions match
                if (nums1[i - 1] == nums2[j - 1]) {
                    // Extend the common subarray length from previous diagonal position
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                    // Update the maximum length if current is larger
                    maxLength = max(maxLength, dp[i][j]);
                }
                // If elements don't match, dp[i][j] remains 0 (already initialized)
            }
        }

        // Return the maximum length of common subarray found
        return maxLength;
    }

    vector<int> dp(n + 1, 0);

    for (int i = 1; i <= m; ++i) {
        for (int j = n; j >= 1; --j) {
            if (nums1[i - 1] == nums2[j - 1]) {
                dp[j] = dp[j - 1] + 1;
            } else {
                dp[j] = 0;
            }
            maxLength = max(maxLength, dp[j]);
        }
    }
};

class Solution {
public:
    int longestCommonSubsequence(string text1, string text2) {
        int m = text1.size();
        int n = text2.size();

        vector<vector<int>> dp(m + 1, vector<int>(n + 1));

        for (int i = 1; i <= m; ++i) {
            for (int j = 1; j <= n; ++j) {
                if (text1[i - 1] == text2[j - 1]) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = max(
                        dp[i - 1][j],
                        dp[i][j - 1]
                    );
                }
            }
        }

        return dp[m][n];
    }
};
