package Companies.GoldmanSachs;

import java.util.ArrayDeque;
import java.util.Deque;

public class ShortestSubarraywithSumatLeastK {
    public int shortestSubarray(int[] A, int K) {
        int re = Integer.MAX_VALUE;
        int[] dp = new int[A.length+1];
        for (int i = 0; i < A.length; i++) {
            dp[i+1] = dp[i] + A[i];
        }
        Deque<Integer> q = new ArrayDeque<>();
        for (int i = 0; i < dp.length; i++) {
            while (q.size() > 0 && dp[i] - dp[q.getFirst()] >= K) {
                re = Math.min(re, i - q.pollFirst());
            }
            while (q.size() > 0 && dp[i] <= dp[q.getLast()]) {
                q.pollLast();
            }
            q.addLast(i);
        }
        return re == Integer.MAX_VALUE? -1:re;
    }
}
