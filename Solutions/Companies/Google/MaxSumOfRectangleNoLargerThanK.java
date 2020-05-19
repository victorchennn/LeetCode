package Companies.Google;

import java.util.TreeSet;

public class MaxSumofRectangleNoLargerThanK {
    public int maxSumSubmatrix(int[][] matrix, int k) {
        int m = matrix.length, n = matrix[0].length, re = Integer.MIN_VALUE;
        for (int l = 0; l < n; l++) {
            int[] sum = new int[m];
            for (int r = l; r < n; r++) {
                for (int row = 0; row < m; row++) {
                    sum[row] += matrix[row][r];
                }
                TreeSet<Integer> set = new TreeSet<>();
                set.add(0);
                int max = Integer.MIN_VALUE, temp = 0;
                for (int i : sum) {
                    temp += i;
                    Integer key = set.ceiling(temp-k);
                    if (key != null) {
                        max = Math.max(max, temp-key);
                    }
                    set.add(temp);
                }
                re = Math.max(re, max);
            }
        }
        return re;
    }
}
