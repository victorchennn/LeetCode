package Companies.Google;

public class RedundantConnectionI {

    int[] dsu;

    public int[] findRedundantConnection(int[][] edges) {
        dsu = new int[2001];
        for (int i = 0; i < dsu.length; i++) {
            dsu[i] = i;
        }
        for (int[] e : edges) {
            int x = e[0], y = e[1];
            if (find(x) == find(y)) {
                return e;
            }
            union(x, y);
        }
        return null;
    }

    private int find(int x) {
        if (dsu[x] != x) {
            dsu[x] = find(dsu[x]);
        }
        return dsu[x];
    }

    private void union(int x, int y) {
        dsu[find(x)] = find(y);
    }
}
