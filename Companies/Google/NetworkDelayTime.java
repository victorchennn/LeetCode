package Companies.Google;

int networkDelayTime(vector<vector<int>>& times, int n, int k) {
    const int INF = 1 << 29;
    vector<vector<int>> graph(n, vector<int>(n, INF));
    for (const auto& edge : times) {
        int from = edge[0] - 1;  // Convert to 0-indexed
        int to = edge[1] - 1;     // Convert to 0-indexed
        int weight = edge[2];
        graph[from][to] = weight;
    }

    vector<int> distance(n, INF);
    distance[k - 1] = 0;  // Distance to source node is 0
  
    // Track visited nodes
    vector<bool> visited(n, false);

    // Dijkstra's algorithm implementation
    for (int i = 0; i < n; ++i) {
        // Find the unvisited node with minimum distance
        int minNode = -1;
        for (int node = 0; node < n; ++node) {
            if (!visited[node] &&
                (minNode == -1 || distance[node] < distance[minNode])) {
                minNode = node;
            }
        }

        if (minNode == -1) {
            break;
        }
      
        // Mark the selected node as visited
        visited[minNode] = true;
      
        // Update distances to all neighbors of the selected node
        for (int neighbor = 0; neighbor < n; ++neighbor) {
            distance[neighbor] = min(distance[neighbor], 
                                    distance[minNode] + graph[minNode][neighbor]);
        }
    }

    int maxDistance = *max_element(distance.begin(), distance.end());

    return maxDistance == INF? -1:maxDistance;
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
