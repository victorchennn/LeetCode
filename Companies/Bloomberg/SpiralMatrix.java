package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> re = new ArrayList<>();
        if (matrix.length == 0 || matrix[0].length == 0) {
            return re;
        }
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        int m = matrix.length, n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];
        int r = 0, c = 0, i = 0;
        for (int index = 0; index < m*n; index++) {
            re.add(matrix[r][c]);
            visited[r][c] = true;
            int rr = r + dirs[i][0];
            int cc = c + dirs[i][1];
            if (rr >= 0 && rr < m && cc >= 0 && cc < n && !visited[rr][cc]) {
                r = rr;
                c = cc;
            } else {
                i = (i+1)%4;
                r += dirs[i][0];
                c += dirs[i][1];
            }
        }
        return re;
    }

    /* generate a square matrix filled with elements from 1 to n^2 in spiral order. */
    public int[][] generateMatrix(int n) {
        int[][] re = new int[n][n];
        int[][] dirs = {{0,1},{1,0},{0,-1},{-1,0}};
        int r = 0, c = 0, i = 0;
        boolean[][] visited = new boolean[n][n];
        for (int num = 1; num <= n*n; num++) {
            re[r][c] = num;
            visited[r][c] = true;
            int rr = r + dirs[i][0];
            int cc = c + dirs[i][1];
            if (rr >= 0 && rr < n && cc >= 0 && cc < n && !visited[rr][cc]) {
                r = rr;
                c = cc;
            } else {
                i = (i+1)%4;
                r += dirs[i][0];
                c += dirs[i][1];
            }
        }
        return re;
    }

    public int[][] spiralMatrixIII(int R, int C, int r0, int c0) {
        int[][] re = new int[R*C][2];
        int[][] dirs = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        int n = 0, index = 0;
        for (int i = 0; i < R*C; n++) {
            for (int j = 0; j < n/2+1; j++) {
                if (r0 >= 0 && r0 < R && c0 >= 0 && c0 < C) {
                    re[i++] = new int[]{r0,c0};
                }
                r0 += dirs[index][0];
                c0 += dirs[index][1];
            }
            index = (index+1)%4;
        }
        return re;
    }
}
