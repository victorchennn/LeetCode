package Companies.Bloomberg;

public class TwoKeysKeyboard {

    /* O(N^{1/2}) */
    public int minSteps(int n) {
        int re = 0, d = 2;
        while (n > 1) {
            while (n%d == 0) {
                re += d;
                n /= d;
            }
            d++;
        }
        return re;

        // int[] dp = new int[n+1];
        // for (int i = 2; i <= n; i++) {
        //     dp[i] = i;
        //     for (int j = i/2; j > 1; j--) {
        //         if (i%j == 0) {
        //             dp[i] = dp[j] + i/j;
        //             break;
        //         }
        //     }
        // }
        // return dp[n];
    }
}
