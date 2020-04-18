package Companies.Google;

public class MinimumSwapsToMakeSequencesIncreasing {
    public int minSwap(int[] A, int[] B) {
        int swap = 1, fix = 0;
        for (int i = 1; i < A.length; i++) {
            if (A[i-1] >= B[i] || B[i-1] >= A[i]) {  // at ith index, swap or fix times.
                swap++;
            } else if (A[i-1] >= A[i] || B[i-1] >= B[i]) {
                int temp = swap;
                swap = fix+1;
                fix = temp;
            } else {
                int min = Math.min(swap, fix);
                swap = min+1;
                fix = min;
            }
        }
        return Math.min(swap, fix);
    }
}
