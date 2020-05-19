package Companies.Amazon;

/**
 * @see WildcardMatching
 */
public class RegularExpressionMatching {
    public boolean isMatch(String s, String p) {
        if (s == null || p == null) {
            return false;
        }
        int m = s.length(), n = p.length();
        boolean[][] dp = new boolean[m+1][n+1];
        dp[0][0] = true;
        for (int i = 0; i < n; i++) {
            if (p.charAt(i) == '*') {
                dp[0][i+1] = dp[0][i-1];
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                    // ######a(i) or #######a(i)
                    // ####a(j)      ####.(j)
                if (p.charAt(j) == '.' || p.charAt(j) == s.charAt(i)) {
                    dp[i+1][j+1] = dp[i][j];
                } else if (p.charAt(j) == '*') {
                        // #####a(i)
                        // ####b*(j)
                    if (p.charAt(j-1) != '.' && p.charAt(j-1) != s.charAt(i)) {
                        dp[i+1][j+1] = dp[i+1][j-1];
                    } else {
                        // ######a(i) or #####a(i)
                        // ####.*(j)     ###a*(j)
                        dp[i+1][j+1] = dp[i][j+1] || dp[i+1][j] || dp[i+1][j-1];
                    }
                }
            }
        }
        return dp[m][n];
    }
}
