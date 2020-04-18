package Companies.Bloomberg;

public class ArithmeticSlices {
    public int numberOfArithmeticSlices(int[] A) {
        int re = 0, count = 0;
        for (int i = 2; i < A.length; i++) {
            if (A[i]-A[i-1] == A[i-1]-A[i-2]) {
                count++;
                re += count;
            } else {
                count = 0;
            }
        }
        return re;
    }
}
