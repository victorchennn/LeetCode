package Companies.Google;

public class SimilarStringGroups {
    public int numSimilarGroups(String[] A) {
        DSU dsu = new DSU(A.length);
        for (int i = 0; i < A.length; i++) {
            for (int j = i+1; j < A.length; j++) {
                if (isSimilar(A[i], A[j])) {
                    dsu.union(i, j);
                }
            }
        }
        int re = 0;
        for (int i = 0; i < A.length; i++) {
            if (dsu.parent[i] == i) {
                re++;
            }
        }
        return re;
    }

    private boolean isSimilar(String s1, String s2) {
        int count = 0;
        for (int i = 0; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                count++;
            }
            if (count > 2) {
                return false;
            }
        }
        return true;
    }

    class DSU {

        int[] parent;

        DSU(int n) {
            parent = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
            }
        }

        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }

        void union(int x, int y) {
            parent[find(x)] = find(y);
        }

    }
}
