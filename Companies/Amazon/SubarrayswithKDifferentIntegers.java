package Companies.Amazon;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return the number of good subarrays of nums, where the number of
 * different integers in that array is exactly k.
 *
 * 1 <= nums[i], k <= nums.length
 */
public class SubarrayswithKDifferentIntegers {
    public int subarraysWithKDistinct(int[] nums, int k) {
        int re = 0, prefix = 0, diff = 0;
        int[] count = new int[nums.length+1];
        for (int l = 0, r = 0; r < nums.length; r++) {
            if (count[nums[r]] == 0) {
                diff += 1;
            }
            count[nums[r]]++;
            if (diff > k) {
                count[nums[l]]--;
                l++;
                diff--;
                prefix = 0;
            }
            while (count[nums[l]] > 1) {
                prefix++;
                count[nums[l]]--;
                l++;
            }
            if (diff == k) {
                re += prefix+1;
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(7, subarraysWithKDistinct(new int[]{1,2,1,2,3}, 2));
        assertEquals(3, subarraysWithKDistinct(new int[]{1,2,1,3,4}, 3));
    }
}
