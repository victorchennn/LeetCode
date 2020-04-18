package Companies.Bloomberg;

public class LongestSubstringPalindromic {
    public String longestPalindrome(String s) {
        int len = s.length();
        if (len == 0) {
            return "";
        }
        boolean[][] dp = new boolean[len][len];
        int l = 0, r = 0;
        for (int i = len-1; i >= 0; i--) {
            for (int j = i; j < len; j++) {
                if (s.charAt(i) == s.charAt(j) && (j-i < 2 || dp[i+1][j-1])) {
                    dp[i][j] = true;
                    if (j-i > r-l) {
                        r = j;
                        l = i;
                    }
                }
            }
        }
        return s.substring(l, r+1);
    }

    private String helper(String s, int l, int r) {
        while (l >= 0 && r < s.length()) {
            if (s.charAt(l) != s.charAt(r)) {
                break;
            }
            l--;
            r++;
        }
        return s.substring(l+1, r);
    }
}
