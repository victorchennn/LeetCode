package Companies.Bloomberg;

import java.util.Arrays;

public class MaximumLengthofPairChain {

    /** Greedy, O(NlogN) */
    public int findLongestChain(int[][] pairs) {
        Arrays.sort(pairs, (a, b)->a[1]-b[1]);
        int pre = pairs[0][1], re = 1;
        for (int i = 1; i < pairs.length; i++) {
            if (pairs[i][0] > pre) {
                re++;
                pre = pairs[i][1];
            }
        }
        return re;
    }

    /** DP, O(N^2) */
    public int findLongestChainII(int[][] pairs) {
        Arrays.sort(pairs, (a,b)->a[0]-b[0]);
        int N = pairs.length;
        int[] dp = new int[N];
        Arrays.fill(dp, 1);

        for (int j = 1; j < N; ++j) {
            for (int i = 0; i < j; ++i) {
                if (pairs[i][1] < pairs[j][0])
                    dp[j] = Math.max(dp[j], dp[i] + 1);
            }
        }

        int ans = 0;
        for (int x: dp) if (x > ans) ans = x;
        return ans;
    }
}
