package Companies.Google;

public class MinimumDominoRotationsForEqualRow {
    public int minDominoRotations(int[] A, int[] B) {
        int[] countA = new int[7];
        int[] countB = new int[7];
        int[] same = new int[7];
        for (int i = 0; i < A.length; i++) {
            countA[A[i]]++;
            countB[B[i]]++;
            if (A[i] == B[i]) {
                same[A[i]]++;
            }
        }
        for (int i = 1; i < 7; i++) {
            if (countA[i]+countB[i]-same[i] >= A.length) {
                return Math.min(countA[i], countB[i]) - same[i];
            }
        }
        return -1;
    }

    public int minDominoRotations2(int[] A, int[] B) {
        for (int i = 0, a = 0, b = 0; i < A.length && (A[i] == A[0] || B[i] == A[0]); i++) {
            if (A[i] != A[0]) {
                a++;
            }
            if (B[i] != A[0]) {
                b++;
            }
            if (i == A.length-1) {
                return Math.min(a, b);
            }
        }
        for (int i = 0, a = 0, b = 0; i < A.length && (A[i] == B[0] || B[i] == B[0]); i++) {
            if (A[i] != B[0]) {
                a++;
            }
            if (B[i] != B[0]) {
                b++;
            }
            if (i == A.length-1) {
                return Math.min(a, b);
            }
        }
        return -1;
    }
}
