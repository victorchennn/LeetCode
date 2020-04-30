package Companies.Google;

import java.util.*;

/**
 * Dijkstra's algorithm
 */
public class NetworkDelayTime {
    public int networkDelayTime(int[][] times, int N, int K) {
        Map<Integer, List<int[]>> m = new HashMap<>();
        boolean[] marks = new boolean[N+1];
        int[] dist = new int[N];
        Arrays.fill(dist, Integer.MAX_VALUE);
        for (int[] time : times) {
            m.computeIfAbsent(time[0], k->new ArrayList<>()).add(new int[]{time[1], time[2]});
        }
        dist[K-1] = 0;
        while (true) {
            int cur = -1;
            int min = Integer.MAX_VALUE;
            for (int i = 1; i <= N; i++) {
                if (dist[i-1] < min && !marks[i]) {
                    min = dist[i-1];
                    cur = i;
                }
            }
            if (cur == -1) {
                break;
            }
            marks[cur] = true;
            if (m.get(cur) != null) {
                for (int[] nei : m.get(cur)) {
                    int target = nei[0];
                    int dis = nei[1];
                    if (!marks[target]) {
                        dist[target-1] = Math.min(dist[target-1], dis+dist[cur-1]);
                    }
                }
            }
        }
        int re = Integer.MIN_VALUE;
        for (int i = 0; i < N; i++) {
            if (dist[i] == Integer.MAX_VALUE) {
                return -1;
            }
            re = Math.max(re, dist[i]);
        }
        return re;
    }
}
