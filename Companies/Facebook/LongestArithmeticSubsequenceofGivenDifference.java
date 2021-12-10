package Companies.Facebook;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given an integer array arr and an integer difference, return the length of the longest
 * subsequence in arr which is an arithmetic sequence such that the difference between
 * adjacent elements in the subsequence equals difference.
 */
public class LongestArithmeticSubsequenceofGivenDifference {
    public int longestSubsequence(int[] arr, int difference) {
        Map<Integer, Integer> dp = new HashMap<>();
        int res = 1;
        for (int n : arr) {
            dp.put(n, dp.getOrDefault(n - difference, 0) + 1);
            res = Math.max(res, dp.get(n));
        }
        return res;
    }

    @Test
    void test() {
        assertEquals(4, longestSubsequence(new int[]{1,2,3,4}, 1));
        assertEquals(1, longestSubsequence(new int[]{1,3,5,7},1));
        assertEquals(4, longestSubsequence(new int[]{1,5,7,8,5,3,4,2,1},-2));
    }
}
