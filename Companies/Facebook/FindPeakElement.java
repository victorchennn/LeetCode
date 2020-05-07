package Companies.Facebook;

/**
 * Given an input array nums, where nums[i] ≠ nums[i+1], find a peak element's index.
 * A peak element is an element that is greater than its neighbors.
 *
 * The array may contain multiple peaks, in that case return the index to any one of the peaks.
 *
 * @see Companies.Bloomberg.PeakIndexinaMountainArray
 */
public class FindPeakElement {
    public int findPeakElement(int[] nums) {
        int l = 0, r = nums.length-1;
        while (l < r) {
            int mid = (l+r)/2;
            if (nums[mid] > nums[mid+1]) {
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return l;
    }
}
