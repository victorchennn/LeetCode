package Topics.DynamicProgramming;

/**
 * find longest palindromic subsequence
 */
public class LongestPalindromicSubsequence {
    public int longestPalindromeSubseq(String s) {
        int len = s.length();
        int[][] dp = new int[len][len];
        for (int i = len-1; i >= 0; i--) {
            dp[i][i] = 1;                   // !!!
            for (int j = i+1; j < len; j++) {
                if (s.charAt(i) == s.charAt(j)) {
                    dp[i][j] = dp[i+1][j-1]+2;
                } else {
                    dp[i][j] = Math.max(dp[i+1][j], dp[i][j-1]);
                }
            }
        }
        return dp[0][len-1];
    }
}
