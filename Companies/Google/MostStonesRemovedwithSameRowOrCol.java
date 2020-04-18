package Companies.Google;

import java.util.HashSet;
import java.util.Set;

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

    public static void main(String...args) {
//        MostStonesRemovedwithSameRowOrCol test = new MostStonesRemovedwithSameRowOrCol();
//        System.out.println(test.removeStones(new int[][]{{0,0},{0,1},{0,2}}));
    }
}


