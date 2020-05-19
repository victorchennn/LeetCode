package Companies.Bloomberg;

public class BipartiteGraph {
    public boolean isBipartite(int[][] graph) {
        int len = graph.length;
        int[] colors = new int[len];
        for (int i = 0; i < len; i++) {
            if (colors[i] == 0 && !dfs(graph, i, 1, colors)) {
                return false;
            }
        }
        return true;
    }

    private boolean dfs(int[][] graph, int i, int color, int[] colors) {
        if (colors[i] != 0) {
            return colors[i] == color;
        }
        colors[i] = color;
        for (int neigh : graph[i]) {
            if (!dfs(graph, neigh, -color, colors)) {
                return false;
            }
        }
        return true;
    }
}
