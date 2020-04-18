package Companies.Bloomberg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IntersectionofTwoArrays {
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
