package Companies.Google;

public class DecodeWays {
    public int numDecodings(String s) {
        if (s == null || s.startsWith("0")) {
            return 0;
        }
        int[] dp = new int[s.length()+1];
        dp[0] = dp[1] = 1;
        for (int i = 2; i <= s.length(); i++) {
            int prepre = 0, pre = 0;
            if (!s.substring(i-2, i).startsWith("0")) {
                int num = Integer.valueOf(s.substring(i-2, i));
                if (num <= 26) {
                    prepre = dp[i-2];
                }
            }
            if (!s.substring(i-1, i).startsWith("0")) {
                pre = dp[i-1];
            }
            dp[i] = pre + prepre;
        }
        return dp[s.length()];
    }
}
