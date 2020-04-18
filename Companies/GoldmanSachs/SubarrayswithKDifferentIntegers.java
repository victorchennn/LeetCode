package Companies.GoldmanSachs;

public class SubarrayswithKDifferentIntegers {

    /**
     * Input: A = [1,2,1,2,3], K = 2
     * Output: 7
     * Explanation: Subarrays formed with exactly 2 different integers:
     * [1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2].
     */
    public int subarraysWithKDistinct(int[] A, int K) {
        int re = 0, prefix = 0;
        int[] dp = new int[A.length+1];
        for (int r = 0, l = 0, count = 0; r < A.length; r++) {
            if (dp[A[r]]++ == 0) {
                count++;
            }
            if (count > K) {
                dp[A[l++]]--;
                count--;
                prefix = 0;
            }
            while (dp[A[l]] > 1) {
                prefix++;
                dp[A[l++]]--;
            }
            if (count == K) {
                re += prefix+1;
            }
        }
        return re;
    }
}
