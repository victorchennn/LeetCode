package Companies.Facebook;

import java.util.Arrays;

public class KnightDialer {
    private int[][] paths = {{4, 6}, {6, 8}, {7, 9}, {4, 8}, {3, 9, 0}, {}, {1, 7, 0}, {2, 6}, {1, 3}, {2, 4}};
    private final int MOD = 1_000_000_007;

    public int knightDialer(int N) {
        int[][] dp = new int[2][10];
        Arrays.fill(dp[1], 1);
        for (int i = 2; i <= N; i++) {
            for (int j = 0; j < 10; j++) {
                dp[i % 2][j] = 0;
                for (int p : paths[j]) {
                    dp[i % 2][j] = (dp[i % 2][j] + dp[(i - 1) % 2][p]) % MOD;
                }
            }
        }
        int res = 0;
        for (int i = 0; i < 10; i++) {
            res = (res + dp[N % 2][i]) % MOD;
        }
        return res;
    }

    public int knightDialerII(int N) {
        int[][] dp = new int[2][10];
        Arrays.fill(dp[0], 1);
        for (int hops = 0; hops < N-1; ++hops) {
            Arrays.fill(dp[~hops & 1], 0);
            for (int node = 0; node < 10; ++node)
                for (int nei: paths[node]) {
                    dp[~hops & 1][nei] += dp[hops & 1][node];
                    dp[~hops & 1][nei] %= MOD;
                }
        }

        long ans = 0;
        for (int x: dp[~N & 1])
            ans += x;
        return (int) (ans % MOD);
    }
}
