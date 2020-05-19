package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArithmeticSlices {
    public int numberOfArithmeticSlices(int[] A) {
        int re = 0, count = 0;
        for (int i = 2; i < A.length; i++) {
            if (A[i]-A[i-1] == A[i-1]-A[i-2]) {
                count++;
                re += count;
            } else {
                count = 0;
            }
        }
        return re;
    }

    /**
     * @Follow-up: Subsequence
     */
    public int numberOfArithmeticSlicesII(int[] A) {
        int n = A.length;
        int ans = 0;
        int[][] dp = new int[n][n];
        Map<Long, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            if (!map.containsKey((long)A[i])) {
                map.put((long)A[i], new ArrayList<>());
            }
            map.get((long)A[i]).add(i);
        }

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < i; j++) {
                long target = 2 * (long)A[j] - A[i];
                if (map.containsKey(target)) {
                    for (int k: map.get(target)) {
                        if (k < j) {
                            dp[i][j] += (dp[j][k] + 1);
                        }
                    }
                }
                ans += dp[i][j];
            }
        }
        return ans;
    }
}
