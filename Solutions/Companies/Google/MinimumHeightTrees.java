package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class MinimumHeightTrees {
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 0) {
            return new ArrayList<>();
        }
        if (n == 1) {
            List<Integer> l = new ArrayList<>();
            l.add(0);
            return l;
        }
        List<Integer>[] lists = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            lists[i] = new ArrayList<>();
        }
        for (int[] e : edges) {
            lists[e[0]].add(e[1]);
            lists[e[1]].add(e[0]);
        }
        List<Integer> leaves = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (lists[i].size() == 1) {
                leaves.add(i);
            }
        }
        int count = n;
        while (count > 2) {
            count -= leaves.size();
            List<Integer> newleaves = new ArrayList<>();
            for (int leave : leaves) {
                for (int neigh : lists[leave]) {
                    lists[neigh].remove(Integer.valueOf(leave));
                    if (lists[neigh].size() == 1) {
                        newleaves.add(neigh);
                    }
                }
            }
            leaves = newleaves;
        }
        return leaves;
    }
}
