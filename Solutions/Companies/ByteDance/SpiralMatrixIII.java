package Companies.ByteDance;

public class SpiralMatrixIII {
    public int[][] spiralMatrixIII(int R, int C, int r0, int c0) {
        int[][] re = new int[R*C][2];
        int[][] dirs = new int[][]{{0,1},{1,0},{0,-1},{-1,0}};
        int n = 0, index = 0;
        for (int i = 0; i < R*C; n++) {
            for (int j = 0; j < n/2+1; j++) {
                if (0 <= r0 && r0 < R && 0 <= c0 && c0 < C) {
                    re[i++] = new int[]{r0, c0};
                }
                r0 += dirs[index][0];
                c0 += dirs[index][1];
            }
            index = (index+1)%4;
        }
        return re;
    }
}
