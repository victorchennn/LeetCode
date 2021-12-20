package Companies.Uber;

/**
 * return the distance of the nearest 0 for each cell.
 */
public class ZeroOneMatrix {
    public int[][] updateMatrix(int[][] mat) {
        int m = mat.length, n = mat[0].length, max = m+n;
        for (int r = 0; r < m; r++) {
            for (int c = 0; c < n; c++) {
                if (mat[r][c] == 0) {
                    continue;
                }
                int top = max, left = max;
                if (r >= 1) {
                    top = mat[r-1][c];
                }
                if (c >= 1) {
                    left = mat[r][c-1];
                }
                mat[r][c] = Math.min(top, left)+1;
            }
        }
        for (int r = m-1; r >= 0; r--) {
            for (int c = n-1; c >= 0; c--) {
                if (mat[r][c] == 0) {
                    continue;
                }
                int bot = max, right = max;
                if (r < m-1) {
                    bot = mat[r+1][c];
                }
                if (c < n-1) {
                    right = mat[r][c+1];
                }
                mat[r][c] = Math.min(mat[r][c], Math.min(bot, right)+1);
            }
        }
        return mat;
    }
}
