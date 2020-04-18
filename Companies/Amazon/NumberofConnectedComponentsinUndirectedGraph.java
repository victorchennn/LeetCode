package Companies.Amazon;

import java.util.ArrayList;
import java.util.List;

public class NumberofConnectedComponentsinUndirectedGraph {
    /** Union-find */
    public int countComponentsI(int n, int[][] edges) {
        int[] dsu = new int[n];
        for (int i = 0; i < n; i++) {
            dsu[i] = i;
        }
        for (int[] edge : edges) {
            int x = find(dsu, edge[0]);
            int y = find(dsu, edge[1]);
            if (x != y) {
                dsu[x] = y;
                n--;
            }
        }
        return n;
    }

    private int find(int[] dsu, int x) {
        if (dsu[x] != x) {
            dsu[x] = find(dsu, dsu[x]);
        }
        return dsu[x];
    }

    /** DFS */
    public int countComponentsII(int n, int[][] edges) {
        List<List<Integer>> l = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            l.add(new ArrayList<>());
        }
        for (int[] edge : edges) {
            l.get(edge[0]).add(edge[1]);
            l.get(edge[1]).add(edge[0]);
        }
        int re = 0;
        boolean[] visited = new boolean[n];
        for (int i = 0; i < n; i++) {
            re += dfs(l, i, visited);
        }
        return re;
    }

    private int dfs(List<List<Integer>> l, int i, boolean[] visited) {
        if (visited[i]) {
            return 0;
        }
        visited[i] = true;
        for (int neighbor : l.get(i)) {
            dfs(l, neighbor, visited);
        }
        return 1;
    }
}
