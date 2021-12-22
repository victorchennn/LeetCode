package Companies.Facebook;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return the length of the longest arithmetic subsequence in nums.
 */
public class LongestArithmeticSubsequence {
    public int longestArithSeqLength(int[] nums) {
        Map<Integer, Integer>[] m = new HashMap[nums.length];
        int re = 2;
        for (int i = 0; i < nums.length; i++) {
            m[i] = new HashMap<>();
            for (int j = 0; j < i; j++) {
                int dis = nums[i]-nums[j];
                m[i].put(dis, m[j].getOrDefault(dis, 1)+1);
                re = Math.max(re, m[i].get(dis));
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(4, longestArithSeqLength(new int[]{3,6,9,12}));
        assertEquals(3, longestArithSeqLength(new int[]{9,4,7,2,10}));
        assertEquals(4, longestArithSeqLength(new int[]{20,1,15,3,10,5,8}));
    }
}
