package Companies.Bloomberg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Number of pairs absolute difference is K
 */
public class KdiffPairsinArray {
    public int findPairs(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }
        Map<Integer, Integer> m = new HashMap<>();
        int re = 0;
        for (int num : nums) {
            if (m.containsKey(num)) {
                if (k == 0 && m.get(num) == 1) {
                    re++;
                    m.put(num, m.get(num)+1);
                }
            } else {
                if (m.containsKey(num-k)) {
                    re++;
                }
                if (m.containsKey(num+k)) {
                    re++;
                }
                m.put(num, 1);
            }
        }
        return re;
    }

    public int findPairsII(int[] nums, int k) {
        if (k < 0) {
            return 0;
        }
        Arrays.sort(nums);
        int l = 0, r = 1, re = 0;
        while (r < nums.length) {
            if (l == r || nums[l] + k > nums[r]) {
                r++;
            } else if (l > 0 && nums[l] == nums[l-1] || nums[l] + k < nums[r]) {
                l++;
            } else {
                re++;
                l++;
            }
        }
        return re;
    }
}
