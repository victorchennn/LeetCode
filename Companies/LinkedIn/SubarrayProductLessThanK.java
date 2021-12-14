package Companies.LinkedIn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return the number of contiguous subarrays where the product of all the elements
 * in the subarray is strictly less than k.
 */
public class SubarrayProductLessThanK {
    public int numSubarrayProductLessThanK(int[] nums, int k) {
        if (k <= 1) {
            return 0;
        }
        int prod = 1, re = 0;
        for (int l = 0, r = 0; r < nums.length; r++) {
            prod *= nums[r];
            while (prod >= k) {
                prod /= nums[l];
                l++;
            }
            re += r-l+1;
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(8, numSubarrayProductLessThanK(new int[]{10,5,2,6},100));
        assertEquals(0, numSubarrayProductLessThanK(new int[]{1,2,3},0));
    }
}
