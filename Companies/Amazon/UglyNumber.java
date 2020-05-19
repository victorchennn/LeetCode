package Companies.Amazon;

public class UglyNumber {
    public boolean isUgly(int num) {
        if (num <= 0) {
            return false;
        }
        for (int i = 2; i <= 5 && num > 0; i++) {
            while (num%i == 0) {
                num /= i;
            }
        }
        return num == 1;
    }

    public int nthUglyNumber(int n) {
        if (n <= 0) {
            return 0;
        }
        int[] dp = new int[n];
        dp[0] = 1;
        int c2 = 0, c3 = 0, c5 = 0;
        for (int i = 1; i < n; i++) {
            dp[i] = Math.min(dp[c2]*2, Math.min(dp[c3]*3, dp[c5]*5));
            if (dp[i] == dp[c2]*2) {
                c2++;
            }
            if (dp[i] == dp[c3]*3) {
                c3++;
            }
            if (dp[i] == dp[c5]*5) {
                c5++;
            }
        }
        return dp[n-1];
    }
}
