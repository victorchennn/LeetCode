package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;

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

    /** Sorted */
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
}
