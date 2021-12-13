package Companies.Uber;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * In one move, you can pick an index i where 0 <= i < nums.length and increment nums[i] by 1.
 *
 * Return the minimum number of moves to make every value in nums unique.
 */
public class MinimumIncrementtoMakeArrayUnique {
    /**
     * Let N be the length of nums and M be the maximum value in nums.
     * Time Complexity: O(N+M)
     */
    public int minIncrementForUnique(int[] nums) {
        int max = 0;
        for (int num : nums) {
            max = Math.max(max, num);
        }
        int[] counts = new int[nums.length+max];
        for (int num : nums) {
            counts[num]++;
        }
        int moves = 0, takens = 0;
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] >= 2) {
                int remove = counts[i]-1;
                takens += remove;
                moves -= remove*i;
            } else if (takens > 0 && counts[i] == 0) {
                takens--;
                moves += i;
            }
        }
        return moves;
    }

    public int minIncrementForUniqueII(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        Arrays.sort(nums);
        int moves = 0, prev = nums[0];
        for (int i = 1; i < nums.length; i++) {
            int expect = prev+1;
            moves += nums[i] > expect? 0:expect-nums[i];
            prev = Math.max(expect, nums[i]);
        }
        return moves;
    }


    @Test
    void test() {
        assertEquals(1, minIncrementForUnique(new int[]{1,2,2}));
        assertEquals(6, minIncrementForUnique(new int[]{3,2,1,2,1,7}));

        assertEquals(1, minIncrementForUniqueII(new int[]{1,2,2}));
        assertEquals(6, minIncrementForUniqueII(new int[]{3,2,1,2,1,7}));
    }
}
