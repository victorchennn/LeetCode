package Companies.Google;

import java.util.Arrays;

public class CampusBikesII {
    public int assignBikes(int[][] workers, int[][] bikes) {
        int n = workers.length;
        int m = bikes.length;
        int[][] dp = new int[n+1][1 << m];
        for (int[] d : dp) {
            Arrays.fill(d, Integer.MAX_VALUE / 2);  // !!!!!
        }
        dp[0][0] = 0;
        int min = Integer.MAX_VALUE;
        for (int i = 1; i <= n; i++) {
            for (int s = 1; s < 1 << m; s++) {
                for (int j = 0; j < m; j++) {
                    if ((s & (1<<j)) == 0) {       // !!!!!
                        continue;
                    }
                    int prev = s ^ (1 << j);       // !!!!!
                    dp[i][s] = Math.min(dp[i-1][prev] + dist(workers[i-1], bikes[j]), dp[i][s]);
                    if (i == n) {
                        min = Math.min(min, dp[i][s]);
                    }
                }
            }
        }
        return min;
    }

    private int dist(int[] worker, int[] bike) {
        return Math.abs(worker[0] - bike[0]) + Math.abs(worker[1] - bike[1]);
    }
}
