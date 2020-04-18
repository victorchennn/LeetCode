package Companies.Google;

public class RedundantConnectionII {

    private int[] parents;

    public int[] findRedundantDirectedConnection(int[][] edges) {
        parents = new int[2001];
        int[] cad1 = {-1, -1}, cad2 = {-1, -1};
        for (int[] e : edges) {
            if (parents[e[1]] == 0) {
                parents[e[1]] = e[0];
            } else {
                cad1 = new int[]{parents[e[1]], e[1]};
                cad2 = new int[]{e[0], e[1]};
                e[1] = 0;  // Be careful !
            }
        }
        for (int i = 0; i < parents.length; i++) {
            parents[i] = i;
        }
        for (int[] e : edges) {
            if (e[1] == 0) {   // Be careful !
                continue;
            }
            int x = parents[e[0]], y = parents[e[1]];
            if (find(x) == find(y)) {
                if (cad1[0] == -1) {
                    return e;
                } else {
                    return cad1;
                }
            }
            union(x, y);
        }
        return cad2;
    }

    private int find(int x) {
        if (parents[x] != x) {
            parents[x] = find(parents[x]);
        }
        return parents[x];
    }

    private void union(int x, int y) {
        parents[find(x)] = find(y);
    }
}
