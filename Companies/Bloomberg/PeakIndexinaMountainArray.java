package Companies.Bloomberg;

/**
 * Let's call an array A a mountain if the following properties hold:
 * A.length >= 3
 * There exists some 0 < i < A.length - 1 such that A[0] < A[1] < ... A[i-1] < A[i] > A[i+1] > ... > A[A.length - 1]
 *
 * @see FindPeakElement
 */
public class PeakIndexinaMountainArray {
    public int peakIndexInMountainArray(int[] A) {
        int l = 0, r = A.length-1, m;
        while (l < r) {
            m = l + (r-l)/2;
            if (A[m] < A[m+1]) {
                l = m+1;
            } else {
                r = m;
            }
        }
        return l;
    }
}
