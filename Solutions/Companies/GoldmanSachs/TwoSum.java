package Companies.GoldmanSachs;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @Follow-up1: find all pairs sum equals target, may contain duplicates
 *
 * @Follow-up2: two byte arrays(only in range [0, 2^8-1]), need to consider overflow error.
 *
 * @Follow-up3: Sorted array
 */
public class TwoSum {
    public int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            if (m.containsKey(target-nums[i])) {
                return new int[]{i, m.get(target-nums[i])};
            }
            m.put(nums[i], i);
        }
        throw new IllegalArgumentException("..");
    }

    /* find all pairs sum equals target, may contain duplicates */
    public List<int[]> findAllTwoSum(int[] nums, int target) {
        Map<Integer, Integer> m = new HashMap<>();
        boolean equalFind = false;
        List<int[]> re = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            if (m.containsKey(nums[i])) {
                if (target%2 == 0 && nums[i] == target/2 && !equalFind) {
                    re.add(new int[]{m.get(nums[i]), i});
                    equalFind = true;
                } else {
                    continue;
                }
            } else if (m.containsKey(target - nums[i])) {
                re.add(new int[]{m.get(target-nums[i]), i});
            } else {
                m.put(nums[i], i);
            }
        }
        return re;
    }

    /* Two byte arrays(only in range [0, 2^8-1]), need to consider overflow error.
     * java byte is in range [-128,127] */
    public int[] twoArraySum(int[] nums1, int[] nums2, int target) {
        boolean[] exists = new boolean[256];
        for (int num : nums1) {
            exists[num] = true;
        }
        for (int num : nums2) {
            if (target - num < 0) {
                continue;
            }
            if (exists[target - num]) {
                return new int[]{num, target-num};
            }
        }
        return new int[]{-1, -1};
    }

    /* Sorted */
    public int[] twoSumII(int[] numbers, int target) {
        int l = 0, r = numbers.length-1;
        while (l < r) {
            if (numbers[l] + numbers[r] == target) {
                return new int[]{l+1, r+1};
            } else if (numbers[l] + numbers[r] > target) {
                r = helper1(numbers, l, r, target-numbers[l]);
            } else {
                l = helper2(numbers, l, r, target-numbers[r]);
            }
        }
        return new int[2];
    }

    private int helper1(int[] nums, int left, int right, int target) {
        int l = left, r = right;
        while (l <= r) {
            int m = l + (r-l)/2;
            if (nums[m] > target) {
                r = m-1;
            } else {
                l = m+1;
            }
        }
        return r;
    }

    private int helper2(int[] nums, int left, int right, int target) {
        int l = left, r = right;
        while (l <= r) {
            int m = l + (r-l)/2;
            if (nums[m] < target) {
                l = m+1;
            } else {
                r = m-1;
            }
        }
        return l;
    }

    @Test
    void test() {
        assertEquals(1, findAllTwoSum(new int[]{2,5,6,3,1}, 9).size());
        assertEquals(2, findAllTwoSum(new int[]{2,5,6,3,1}, 8).size());
        assertEquals(2, findAllTwoSum(new int[]{2,2,5,6,3,1}, 8).size());
        assertEquals(2, findAllTwoSum(new int[]{2,2,4,5,6,3,1}, 8).size());
        assertEquals(3, findAllTwoSum(new int[]{2,2,4,4,5,6,3,1}, 8).size());
        assertEquals(3, findAllTwoSum(new int[]{2,2,4,4,4,5,6,3,1}, 8).size());
    }
}
