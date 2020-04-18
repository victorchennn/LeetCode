package Companies.Bloomberg;

public class MaximalRectangle {

    /**
     * Input:
     * [["1","0","1","0","0"],
     *  ["1","0","1","1","1"],
     *  ["1","1","1","1","1"],
     *  ["1","0","0","1","0"]]
     * Output: 6
     */
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        int m = matrix.length, n = matrix[0].length, max = 0;
        int[][] heights = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = n-1; j >= 0; j--) {
                if (matrix[i][j] == '1') {
                    int height = i == 0?1:heights[i-1][j]+1;
                    heights[i][j] = height;
                    max = Math.max(max, height);
                    for (int p = j+1; p < n && matrix[i][p] == '1'; p++) {
                        height = Math.min(height, heights[i][p]);
                        max = Math.max(max, height*(p-j+1));
                    }
                }
            }
        }
        return max;
    }
}
