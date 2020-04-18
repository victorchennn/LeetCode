package Companies.ByteDance;

public class SpiralMatrixII {
    public int[][] generateMatrix(int n) {
        int[][] re = new int[n][n];
        int[][] dirs = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        int r = 0, c = 0, index = 0;
        for (int i = 1; i <= n*n; i++) {
            re[r][c] = i;
            int rr = r + dirs[index][0];
            int cc = c + dirs[index][1];
            if (rr >= 0 && cc >= 0 && rr < n && cc < n && re[rr][cc] == 0) {
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
