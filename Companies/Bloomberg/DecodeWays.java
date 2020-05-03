package Companies.Bloomberg;

/**
 * number of ways to form a string
 * Input: "226"
 * Output: 3
 * Explanation: It could be decoded as "BZ" (2 26), "VF" (22 6), or "BBF" (2 2 6).
 */
public class DecodeWays {
    public int numDecodings(String s) {
        if (s.length() == 0 || s.startsWith("0")) {
            return 0;
        }
        int[] dp = new int[s.length()+1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= s.length(); i++) {
            int pre = 0, prepre = 0;
            if (!s.substring(i-2, i).startsWith("0")) {
                int num = Integer.valueOf(s.substring(i-2, i));
                if (num <= 26) {
                    prepre = dp[i-2];
                }
            }
            if (!s.substring(i-1, i).startsWith("0")) {
                pre = dp[i-1];
            }
            dp[i] = prepre + pre;
        }
        return dp[s.length()];
    }
}
