package Companies.Facebook;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given an array of positive integers, find three non-overlapping subarrays with maximum sum.
 * Each subarray will be of size k, and we want to maximize the sum of all 3*k entries.
 *
 * Input: [1,2,1,2,6,7,5,1], 2
 * Output: [0,3,5]
 *
 * Explanation: Subarrays [1, 2], [2, 6], [7, 5] correspond to the starting indices [0, 3, 5].
 * We could have also taken [2, 1], but an answer of [1, 3, 5] would be lexicographically larger.
 */
public class MaximumSumof3NonOverlappingSubarrays {
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int[] wins = new int[nums.length-k+1];  // window sum
        int curSum = 0;
        for (int i = 0; i < nums.length; i++) {
            curSum += nums[i];
            if (i >= k) {
                curSum -= nums[i-k];
            }
            if (i >= k-1) {
                wins[i-k+1] = curSum;
            }
        }

        int[] lMax = new int[wins.length];
        int[] rMax = new int[wins.length];
        int p = 0;
        for (int i = 0; i < wins.length; i++) {
            if (wins[i] > wins[p]) {
                p = i;
            }
            lMax[i] = p;
        }
        p = wins.length-1;
        for (int i = wins.length-1; i >= 0; i--) {
            if (wins[i] >= wins[p]) { // need the lexicographically smallest one
                p = i;
            }
            rMax[i] = p;
        }

        int[] re = new int[]{-1, -1, -1};
        for (int m = k; m < wins.length-k; m++) {
            int l = lMax[m-k], r = rMax[m+k];
            if (re[0] == -1 || wins[l]+wins[r]+wins[m] > wins[re[0]]+wins[re[1]]+wins[re[2]]) {
                re[0] = l;
                re[1] = m;
                re[2] = r;
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(true, Arrays.equals(new int[]{0,3,5},
                maxSumOfThreeSubarrays(new int[]{1,2,1,2,6,7,5,1}, 2)));
    }
}
