package Companies.Amazon;

/**
 * Picking the cell at coordinates (r, c) will add points[r][c] to your score.
 * picking cells at coordinates (r, c1) and (r + 1, c2) will subtract abs(c1 - c2) from your score.
 */
public class MaximumNumberofPointswithCost {
    public long maxPoints(int[][] points) {
        int rows = points.length, cols = points[0].length;
        long[] dp = new long[cols];
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                dp[c] += points[r][c];
            }
            for (int c = 1; c < cols; c++) {
                dp[c] = Math.max(dp[c], dp[c-1]-1);
            }
            for (int c = cols-2; c >= 0; c--) {
                dp[c] = Math.max(dp[c], dp[c+1]-1);
            }
        }
        long re = 0;
        for (long val : dp) {
            re = Math.max(re, val);
        }
        return re;
    }
}
