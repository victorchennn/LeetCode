package Companies.Facebook;

/**
 * find longest palindromic substring
 */
public class LongestPalindromicSubstring {
    public String longestPalindrome_s1(String s) {
        String re = "";
        for (int i = 0; i < s.length(); i++) {
            String len1 = helper(s, i, i);
            String len2 = helper(s, i, i+1);
            if (len1.length() > re.length()) {
                re = len1;
            }
            if (len2.length() > re.length()) {
                re = len2;
            }
        }
        return re;
    }

    public String longestPalindrome_s2(String s) {
        int len = s.length();
        if (len == 0) {
            return "";
        }
        boolean[][] dp = new boolean[len][len];
        int start = 0, end = 0;
        for (int i = len-1; i >= 0; i--) {
            for (int j = i; j < len; j++) {
                dp[i][j] = (s.charAt(i) == s.charAt(j) && (j-i < 2 || dp[i+1][j-1]));
                if (dp[i][j] && j-i > end-start) {
                    start = i;
                    end = j;
                }
            }
        }
        return s.substring(start, end+1);
    }


    private String helper(String s, int i, int j) {
        for (; i >= 0 && j < s.length(); i--, j++) {
            if (s.charAt(i) != s.charAt(j)) {
                break;
            }
        }
        return s.substring(i+1, j);
    }
}
