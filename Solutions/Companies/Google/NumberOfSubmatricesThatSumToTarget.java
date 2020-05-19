package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class NumberofSubmatricesThatSumToTarget {
    public int numSubmatrixSumTarget(int[][] matrix, int target) {
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                matrix[i][j] += matrix[i][j-1];
            }
        }
        int re = 0;
        for (int l = 0; l < n; l++) {
            for (int r = l; r < n; r++) {
                Map<Integer, Integer> map = new HashMap<>();
                map.put(0, 1);
                int cur = 0;
                for (int i = 0; i < m; i++) {
                    cur += matrix[i][r] - (l==0?0:matrix[i][l-1]);
                    re += map.getOrDefault(cur-target, 0);
                    map.put(cur, map.getOrDefault(cur, 0)+1);
                }
            }
        }
        return re;
    }
}
