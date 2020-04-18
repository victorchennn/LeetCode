package Companies.Google;

public class DiagonalTraverse {
    public int[] findDiagonalOrder(int[][] matrix) {
        if (matrix == null || matrix.length == 0) return new int[0];
        int m = matrix.length, n = matrix[0].length;
        int[] re = new int[m * n];
        int row = 0, col = 0, d = 1;
        for (int i = 0; i < m*n; i++) {
            re[i] = matrix[row][col];
            row -= d;
            col += d;
            if (row >= m) { // be careful of order
                row = m-1;
                col += 2;
                d = -d;
            }
            if (col >= n) {
                col = n-1;
                row += 2;
                d = -d;
            }
            if (row < 0) {
                row = 0;
                d = -d;
            }
            if (col < 0) {
                col = 0;
                d = -d;
            }
        }
        return re;
    }
}
