package Companies.Google;

import java.util.Arrays;

public class SquaresOfASortedArray {
    public int[] sortedSquares(int[] A) {
        int i = Arrays.binarySearch(A, 0);
        if (i < 0) {
            i = -(i+1);
        }
        int j = i-1;
        int[] re = new int[A.length];
        int index = 0;
        while (j >= 0 && i < A.length) {
            if (-A[j] > A[i]) {
                re[index++] = A[i]*A[i];
                i++;
            } else {
                re[index++] = A[j]*A[j];
                j--;
            }
        }
        while (j >= 0) {
            re[index++] = A[j]*A[j];
            j--;
        }
        while (i < A.length) {
            re[index++] = A[i]*A[i];
            i++;
        }
        return re;
    }
}
