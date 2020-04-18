package Companies.Microsoft;

import java.util.*;

public class PerfectSquares {
    public int numSquares(int n) {
        int[] dp = new int[n+1];
        Arrays.fill(dp, Integer.MAX_VALUE);
        dp[0] = 0;
        for (int i = 1; i <= n; i++) {
            for (int j = 1; j*j <= i; j++) {
                dp[i] = Math.min(dp[i], dp[i-j*j]+1);
            }
        }
        return dp[n];
    }

    /* BFS */
    public int numSquaresII(int n) {
        Queue<Integer> q = new LinkedList<>();
        Set<Integer> s = new HashSet<>();
        q.add(0);
        s.add(0);
        int depth = 0;
        while (!q.isEmpty()) {
            depth++;
            int size = q.size();
            while (size-- > 0) {
                int cur = q.poll();
                for (int i = 1; i*i <= n; i++) {
                    int sum = cur+i*i;
                    if (sum == n) {
                        return depth;
                    }
                    if (sum > n) {
                        break;
                    }
                    if (!s.contains(sum)) {
                        s.add(sum);
                        q.add(sum);
                    }
                }
            }
        }
        return depth;
    }
}
