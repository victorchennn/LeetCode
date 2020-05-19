package Companies.Google;

public class RotatedDigits {
    public int rotatedDigits(int N) {
        int[] dp = new int[N+1];
        int re = 0;
        for (int i = 0; i <= N; i++) {
            if (i < 10) {
                if (i == 0 || i == 1 || i == 8) {
                    dp[i] = 1;
                } else if (i == 2 || i == 5 || i == 6 || i == 9) {
                    dp[i] = 2;
                    re++;
                }
            } else {
                int carry = dp[i%10], rest = dp[i/10];
                if (carry == 1 && rest == 1) {
                    dp[i] = 1;
                } else if (carry >= 1 && rest >= 1) {
                    dp[i] = 2;
                    re++;
                }
            }
        }
        return re;
    }
}
