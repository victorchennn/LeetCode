package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class SpiralMatrix {
    public List<Integer> spiralOrder(int[][] matrix) {
        List<Integer> re = new ArrayList<>();
        if (matrix == null || matrix.length == 0) {
            return re;
        }
        int m = matrix.length, n = matrix[0].length;
        boolean[][] marks = new boolean[m][n];
        int[][] dirs = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        int r = 0, c = 0, index = 0;
        for (int i = 0; i < m*n; i++) {
            re.add(matrix[r][c]);
            marks[r][c] = true;
            int rr = r + dirs[index][0];
            int cc = c + dirs[index][1];
            if (rr >= 0 && cc >= 0 && rr < m && cc < n && !marks[rr][cc]) {
                r = rr;
                c = cc;
            } else {
                index = (index+1)%4;
                r += dirs[index][0];
                c += dirs[index][1];
            }
        }
        return re;
    }
}
