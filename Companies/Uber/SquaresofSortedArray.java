package Companies.Uber;

public class SquaresofSortedArray {
    public int[] sortedSquares(int[] A) {
        int[] re = new int[A.length];
        int l = 0, r = A.length-1;
        for (int i = A.length-1; i >= 0; i--) {
            if (Math.abs(A[l]) > Math.abs(A[r])) {
                re[i] = A[l]*A[l];
                l++;
            } else {
                re[i] = A[r]*A[r];
                r--;
            }
        }
        return re;
    }
}
