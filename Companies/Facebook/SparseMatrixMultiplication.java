package Companies.Facebook;

public class SparseMatrixMultiplication {
    public int[][] multiply(int[][] A, int[][] B) {
        int lenA = A.length, len = A[0].length, lenB = B[0].length;
        int[][] re = new int[lenA][lenB];
        for (int i = 0; i < lenA; i++) {
            for (int j = 0; j < len; j++) {
                if (A[i][j] != 0) {
                    for (int k = 0; k < lenB; k++) {
                        if (B[j][k] != 0) {
                            re[i][k] += A[i][j]*B[j][k];
                        }
                    }
                }
            }
        }
        return re;
    }
}
