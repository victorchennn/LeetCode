package Companies.Bloomberg;

public class CountPrimes {
    public int countPrimes(int n) {
        boolean[] dp = new boolean[n];
        if (n < 2) {
            return 0;
        }
        int count = 2; // 0 and 1
        for (int i = 2; i < Math.sqrt(n); i++) {
            if (!dp[i]) {
                for (int j = i*i; j < n; j += i) {
                    if (dp[j]) {
                        continue;
                    }
                    count++;
                    dp[j] = true;
                }
            }
        }
        return n - count;
    }
}
