package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacificAtlanticWaterFlow {
    private int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public List<List<Integer>> pacificAtlantic(int[][] matrix) {
        List<List<Integer>> re = new ArrayList<>();
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return re;
        }
        int m = matrix.length, n = matrix[0].length;
        boolean[][] pa = new boolean[m][n];
        boolean[][] at = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            dfs(matrix, pa, i, 0, Integer.MIN_VALUE);
            dfs(matrix, at, i, n-1, Integer.MIN_VALUE);
        }
        for (int i = 0; i < n; i++) {
            dfs(matrix, pa, 0, i, Integer.MIN_VALUE);
            dfs(matrix, at, m-1, i, Integer.MIN_VALUE);
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (pa[i][j] && at[i][j]) {
                    re.add(Arrays.asList(i, j));
                }
            }
        }
        return re;
    }

    private void dfs(int[][] matrix, boolean[][] ocean, int i, int j, int prev) {
        if (i < 0 || j < 0 || i >= matrix.length || j >= matrix[0].length || matrix[i][j] < prev || ocean[i][j]) {
            return;
        }
        ocean[i][j] = true;
        for (int[] dir : dirs) {
            dfs(matrix, ocean, i + dir[0], j + dir[1], matrix[i][j]);
        }
    }
}
