package Companies.Bloomberg;

public class ShortestPalindrome {
    public String shortestPalindrome(String s) {
        String temp = s + "#" + new StringBuilder(s).reverse().toString();
        int[] dp = new int[temp.length()];
        for (int i = 1; i < dp.length; i++) {
            int j = dp[i - 1];
            while (j > 0 && temp.charAt(i) != temp.charAt(j)) {
                j = dp[j - 1];
            }
            if (temp.charAt(i) == temp.charAt(j)) {
                dp[i] = j + 1;
            }
        }
        int index = dp[dp.length-1];
        return new StringBuilder(s.substring(index)).reverse().toString() + s;
    }
}
