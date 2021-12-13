package Companies.Uber;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * union and find functions have worst case O(N), amortize O(1)
 * The whole union-find solution with path compression, has O(N) Time, O(N) Space
 */
public class MostStonesRemovedwithSameRowOrCol {
    public int removeStones(int[][] stones) {
        DSU dsu = new DSU(20000);
        for (int[] stone : stones) {
            dsu.union(stone[0], stone[1]+10000);
        }
        Set<Integer> set = new HashSet<>();
        for (int[] stone : stones) {
            set.add(dsu.find(stone[0]));
        }
        return stones.length - set.size();
    }

    class DSU {
        int[] parents;

        DSU(int N) {
            parents = new int[N];
            for (int i = 0; i < N; i++) {
                parents[i] = i;
            }
        }

        int find(int x) {
            if (parents[x] != x) {
                parents[x] = find(parents[x]);
            }
            return parents[x];
        }

        void union(int x, int y) {
            parents[find(x)] = find(y);
        }
    }

    @Test
    void test() {
        assertEquals(5, removeStones(new int[][]{{0,0},{0,1},{1,0},{1,2},{2,1},{2,2}}));
        assertEquals(3, removeStones(new int[][]{{0,0},{0,2},{1,1},{2,0},{2,2}}));
        assertEquals(0, removeStones(new int[][]{{0,0}}));
    }
}


