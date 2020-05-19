package Companies.Microsoft;

import java.util.*;

/**
 * @Follow-up:
 * 1. What if the given array is already sorted? How would you optimize your algorithm?
 *    The sorting way, it will give us linear time and constant memory complexity.
 *
 * 2. What if nums1's size is small compared to nums2's size? Which algorithm is better?
 *    Use a HashMap for the smaller array.
 *
 * 3. What if elements of nums2 are stored on disk, and the memory is limited such that you
 * cannot load all elements into the memory at once?
 *
 *    - If nums1 fits into the memory, we can use HashMap way to collect counts for nums1 into a hash map.
 *    Then, we can sequentially load and process nums2.
 *
 *    - If neither of the arrays fit into the memory, we can apply some partial processing strategies:
 *      a. Split the numeric range into subranges that fits into the memory. Modify HashMap way to collect
 *      counts only within a given subrange, and call the method multiple times (for each subrange).
 *
 *      b. Use an external sort for both arrays. Modify Sorting way to load and process arrays sequentially.
 *
 */
public class IntersectionofTwoArrays {

    /**
     * Input: nums1 = [1,2,2,1], nums2 = [2,2]
     * Output: [2]
     *
     * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * Output: [9,4]
     */
    public int[] intersection(int[] nums1, int[] nums2) {
        Set<Integer> set = new HashSet<>();
        Set<Integer> intersect = new HashSet<>();
        for (int num : nums1) {
            set.add(num);
        }
        for (int num : nums2) {
            if (set.contains(num)) {
                intersect.add(num);
            }
        }
        int[] result = new int[intersect.size()];
        int i = 0;
        for (int num : intersect) {
            result[i++] = num;
        }
        return result;
    }

    /**
     * Input: nums1 = [1,2,2,1], nums2 = [2,2]
     * Output: [2,2]
     *
     * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
     * Output: [4,9]
     */
    public int[] intersect(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return intersect(nums2, nums1);
        }
        Map<Integer, Integer> m = new HashMap<>();
        for (int num : nums1) {
            m.put(num, m.getOrDefault(num, 0)+1);
        }
        int index = 0;
        for (int num : nums2) {
            int count = m.getOrDefault(num, 0);
            if (count > 0) {
                m.put(num, count-1);
                nums1[index++] = num;
            }
        }
        return Arrays.copyOfRange(nums1, 0, index);
    }

    public int[] intersectII(int[] nums1, int[] nums2) {
        Arrays.sort(nums1);
        Arrays.sort(nums2);
        int i = 0, j = 0, k = 0;
        while (i < nums1.length && j < nums2.length) {
            if (nums1[i] < nums2[j]) {
                ++i;
            } else if (nums1[i] > nums2[j]) {
                ++j;
            } else {
                nums1[k++] = nums1[i];
                i++;
                j++;
            }
        }
        return Arrays.copyOfRange(nums1, 0, k);
    }
}
