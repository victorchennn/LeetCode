package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class AllPathsFromSourcetoTarget {
    public List<List<Integer>> allPathsSourceTarget(int[][] graph) {
        List<List<Integer>> re = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        cur.add(0);
        dfs(re, cur, 0, graph);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> cur, int p, int[][] graph) {
        if (p == graph.length-1) {
            re.add(new ArrayList<>(cur));
            return;
        }
        for (int next : graph[p]) {
            cur.add(next);
            dfs(re, cur, next, graph);
            cur.remove(cur.size()-1);
        }

    }
}
