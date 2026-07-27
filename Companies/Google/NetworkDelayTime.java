package Companies.Google;

int networkDelayTime(vector<vector<int>>& times, int n, int k) {
    Map<Integer, List<int[]>> graph = new HashMap<>();

    for (int[] edge : times) {
        int from = edge[0];
        int to = edge[1];
        int weight = edge[2];

        graph.computeIfAbsent(from, key -> new ArrayList<>())
             .add(new int[]{to, weight});
    }

    int[] dist = new int[n + 1];
    Arrays.fill(dist, Integer.MAX_VALUE);
    dist[k] = 0;

    // {distance, node}，按照 distance 从小到大排列
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));

    pq.offer(new int[]{0, k});

    while (!pq.isEmpty()) {
        int[] current = pq.poll();
        int currentDistance = current[0];
        int currentNode = current[1];

        // 这是一条旧记录，跳过
        if (currentDistance > dist[currentNode]) {
            continue;
        }

        for (int[] neighbor : graph.getOrDefault(currentNode, Collections.emptyList())) {
            int nextNode = neighbor[0];
            int weight = neighbor[1];
            int newDistance = currentDistance + weight;
            if (newDistance < dist[nextNode]) {
                dist[nextNode] = newDistance;
                pq.offer(new int[]{newDistance, nextNode});
            }
        }
    }

    int answer = 0;
    for (int node = 1; node <= n; node++) {
        if (dist[node] == Integer.MAX_VALUE) {
            return -1;
        }
        answer = Math.max(answer, dist[node]);
    }
    return answer;
}



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
