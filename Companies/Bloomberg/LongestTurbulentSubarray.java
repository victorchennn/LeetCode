package Companies.Bloomberg;

/**
 * Input: [9,4,2,10,7,8,8,1,9]
 * Output: 5
 * Explanation: (A[1] > A[2] < A[3] > A[4] < A[5])
 */
public class LongestTurbulentSubarray {
    public int maxTurbulenceSize(int[] A) {
        int up = 1, down = 1, re = 1;
        for (int i = 1; i < A.length; i++) {
            if (A[i] < A[i-1]) {
                down = up+1;
                up = 1;
            } else if (A[i] > A[i-1]) {
                up = down+1;
                down = 1;
            } else {
                up = 1;
                down = 1;
            }
            re = Math.max(re, Math.max(up, down));
        }
        return re;
    }
}
