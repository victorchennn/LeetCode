package Companies.Bloomberg;

public class LargestSumofAverages {

    /* O(KN^2) */
    public double largestSumOfAverages(int[] A, int K) {
        int len = A.length;
        double[][] dp = new double[len+1][K+1];
        double sum = 0;
        for (int i = 0; i < A.length; i++) {
            sum += A[i];
            dp[i+1][1] = sum/(i+1);
        }
        return dfs(dp, A, len, K);
    }

    private double dfs(double[][] dp, int[] A, int len, int K) {
        if (dp[len][K] != 0) {
            return dp[len][K];
        }
        if (len < K) {
            return 0;
        }
        double sum = 0;
        for (int i = len-1; i >= 0; i--) {
            sum += A[i];
            dp[len][K] = Math.max(dp[len][K], sum/(len-i) + dfs(dp, A, i, K-1));
        }
        return dp[len][K];
    }
}
