package Companies.Google;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given an array nums of even length n, pair up the elements of nums into n / 2 pairs such that:
 *
 * Each element of nums is in exactly one pair, and
 * The maximum pair sum is minimized.
 *
 * Return the minimized maximum pair sum after optimally pairing up the elements.
 */
public class MinimizeMaximumPairSuminArray {
    public int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int res = 0, n = nums.length;
        for (int i = 0; i < n / 2; i++)
            res = Math.max(res, nums[i] + nums[n - i - 1]);
        return res;
    }

    /**
     * Used when the max value of nums is not too large
     */
    public int minPairSumII(int[] nums) {
        int max = Integer.MIN_VALUE;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        int[] count = new int[max+1];
        for (int num : nums) {
            count[num]++;
        }
        int l = 0, r = count.length-1;
        while (l < r) {
            if (count[l] != 0 && count[r] != 0) {
                max = Math.max(max, l+r);
                count[l]--;
                count[r]--;
            }
            if (count[l] == 0) {
                l++;
            }
            if (count[r] == 0) {
                r--;
            }
        }
        if (count[r] != 0) {
            max = Math.max(max, r+r);
        }
        return max;
    }

    @Test
    void test() {
        assertEquals(7, minPairSum(new int[]{3,5,2,3}));
        assertEquals(8, minPairSum(new int[]{3,5,4,2,4,6}));
        assertEquals(7, minPairSumII(new int[]{3,5,2,3}));
        assertEquals(8, minPairSumII(new int[]{3,5,4,2,4,6}));
    }
}
