package Companies.Google;

public class LongestIncreasingPathInAMatrix {

    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public int longestIncreasingPath(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int max = 0;
        int[][] cache = new int[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                max = Math.max(max, dfs(matrix, cache, i, j));
            }
        }
        return max;
    }

    private int dfs(int[][] matrix, int[][] cache, int i, int j) {
        if (cache[i][j] != 0) {
            return cache[i][j];
        }
        int max = 1;
        for (int[] dir : dirs) {
            int r = i + dir[0];
            int c = j + dir[1];
            if (r < 0 || c < 0 || r >= cache.length || c >= cache[0].length || matrix[r][c] <= matrix[i][j]) {
                continue;
            }
            max = Math.max(max, 1 + dfs(matrix, cache, r, c));
        }
        cache[i][j] = max;
        return max;
    }
}
