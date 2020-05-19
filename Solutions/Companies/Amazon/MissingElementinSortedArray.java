package Companies.Amazon;

/**
 * Find Kth missing number is a sorted array.
 * Input: A = [4,7,9,10], K = 3
 * Output: 8
 * Explanation: The missing numbers are [5,6,8,...], hence the third missing number is 8.
 */
public class MissingElementinSortedArray {
    public int missingElement(int[] nums, int k) {
        int len = nums.length, l = 0, r = len-1;
        int mis = nums[r] - nums[l] + 1 - len;
        if (mis < k) {
            return nums[r] + k - mis;
        }
        while (l < r-1) {
            int m = l + (r-l)/2;
            mis = nums[m]-nums[l] - (m-l);
            if (mis < k) {
                l = m;
                k -= mis;
            } else {
                r = m;
            }
        }
        return nums[l] + k;
    }
}
