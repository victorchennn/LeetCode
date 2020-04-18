package Companies.Microsoft.Binary;

public class MedianofTwoSortedArrays {
    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length, n = nums2.length;
        if (m > n) {
            int temp = m; m = n; n = temp;
            int[] tep = nums1; nums1 = nums2; nums2 = tep;
        }
        int imin = 0, imax = m, half = (m+n+1)/2;
        while (imin <= imax) {
            int i = (imin + imax)/2;
            int j = half-i;
            if (i > imin && nums1[i-1] > nums2[j]) {
                imax = i-1;
            } else if (i < imax && nums1[i] < nums2[j-1]) {
                imin = i+1;
            } else {
                int maxLeft = 0;
                if (i == 0) {
                    maxLeft = nums2[j-1];
                } else if (j == 0) {
                    maxLeft = nums1[i-1];
                } else {
                    maxLeft = Math.max(nums1[i-1], nums2[j-1]);
                }
                if ((m+n)%2 == 1) {
                    return maxLeft;
                }
                int minRight = 0;
                if (i == m) {
                    minRight = nums2[j];
                } else if (j == n) {
                    minRight = nums1[i];
                } else {
                    minRight = Math.min(nums1[i], nums2[j]);
                }
                return (maxLeft+minRight)/2.0;
            }
        }
        return 0;
    }
}
