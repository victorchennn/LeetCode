package Topics.BinarySearch;

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
