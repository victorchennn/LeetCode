package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaxConsecutiveOnes {
    /**
     * return the maximum number of consecutive 1's in the array.
     */
    public int findMaxConsecutiveOnes(int[] nums) {
        int re = 0, sum = 0;
        for (int num : nums) {
            sum += num;
            if (num == 0) {
                sum = 0;
            } else {
                re = Math.max(re, sum);
            }
        }
        return re;
    }

    /**
     * return the maximum number of consecutive 1's in the array if you can flip at most k 0's.
     */
    public int longestOnes(int[] nums, int k) {
        int l = 0, r = 0;
        while (r < nums.length) {
            if (nums[r] == 0) {
                k--;
            }
            if (k < 0) {
                k += 1-nums[l];
                l++;
            }
            r++;
        }
        return r-l;
    }

    @Test
    void test() {
        assertEquals(3, findMaxConsecutiveOnes(new int[]{1,1,0,1,1,1}));
        assertEquals(2, findMaxConsecutiveOnes(new int[]{1,0,1,1,0,1}));

        assertEquals(6, longestOnes(new int[]{1,1,1,0,0,0,1,1,1,1,0}, 2));
        assertEquals(10, longestOnes(new int[]{0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1}, 3));
    }
}
