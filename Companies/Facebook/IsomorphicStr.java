package Companies.Facebook;

public class IsomorphicStr {
    public boolean isIsomorphic(String s, String t) {
        int[] dp = new int[512];
        for (int i = 0; i < s.length(); i++) {
            if (dp[s.charAt(i)] != dp[t.charAt(i)+256]) {
                return false;
            }
            dp[s.charAt(i)] = i+1;
            dp[t.charAt(i)+256] = i+1;
        }
        return true;
    }
}
