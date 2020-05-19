package Companies.Facebook;

public class MergeSortedArray {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int len1 = m-1, len2 = n-1, len = m+n-1;
        while (len1 >= 0 && len2 >= 0) {
            nums1[len--] = nums1[len1] < nums2[len2]? nums2[len2--]:nums1[len1--];
        }
        System.arraycopy(nums2, 0, nums1, 0, len2+1);
    }
}
