package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class FindEventualSafeStates {
    public List<Integer> eventualSafeNodes(int[][] graph) {
        List<Integer> re = new ArrayList<>();
        int[] color = new int[graph.length];
        for (int i = 0; i < graph.length; i++) {
            if (dfs(graph, color, i)) {
                re.add(i);
            }
        }
        return re;
    }

    private boolean dfs(int[][] graph, int[] color, int i) {
        if (color[i] != 0) {
            return color[i] == 2;
        }
        color[i] = 1;
        for (int neigh : graph[i]) {
            if (color[neigh] == 2) {
                continue;
            }
            if (!dfs(graph, color, neigh)) {
                return false;
            }
        }
        color[i] = 2;
        return true;
    }
}
