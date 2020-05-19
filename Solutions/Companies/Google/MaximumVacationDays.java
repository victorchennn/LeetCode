package Companies.Google;

import java.util.Arrays;

/**
 * n is the number of cities, k is the total number of weeks.
 * Time Complexity: O(n^2k)
 * Space Complexity: O(nk)
 */
public class MaximumVacationDays {
    public int maxVacationDays(int[][] flights, int[][] days) {
        int cities = flights.length, weeks = days[0].length;
        int[] dp = new int[cities];
        Arrays.fill(dp, Integer.MIN_VALUE);
        dp[0] = 0;

        for (int w = 0; w < weeks; w++) {
            int[] temp = new int[cities];
            Arrays.fill(temp, Integer.MIN_VALUE);
            for (int c1 = 0; c1 < cities; c1++) {
                for (int c2 = 0; c2 < cities; c2++) {
                    if (c1 == c2 || flights[c2][c1] == 1) {
                        temp[c1] = Math.max(temp[c1], dp[c2] + days[c1][w]);
                    }
                }
            }
            dp = temp;
        }
        int max = 0;
        for (int v : dp) {
            max = Math.max(max, v);
        }
        return max;
    }
}
