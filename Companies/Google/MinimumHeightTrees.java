package Companies.Google;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * For an undirected graph with tree characteristics, we can choose any node as the root.
 * The result graph is then a rooted tree. Among all possible rooted trees, those with minimum
 * height are called minimum height trees (MHTs).
 *
 * Given such a graph, write a function to find all the MHTs and return a list of their root labels.
 */
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
                    lists[neigh].remove(Integer.valueOf(leave)); // remove the object, not index
                    if (lists[neigh].size() == 1) {
                        newleaves.add(neigh);
                    }
                }
            }
            leaves = newleaves;
        }
        return leaves;
    }

    @Test
    void test() {
        assertEquals(true, findMinHeightTrees(4,
                new int[][]{{1,0},{1,2},{1,3}}).equals(new ArrayList<>(Arrays.asList(1))));
        assertEquals(true, findMinHeightTrees(6,
                new int[][]{{0,3},{1,3},{2,3},{4,3},{5,4}}).equals(new ArrayList<>(Arrays.asList(3,4))));
    }
}
