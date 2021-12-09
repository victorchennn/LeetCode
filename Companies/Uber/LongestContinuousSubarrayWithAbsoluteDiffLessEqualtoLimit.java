package Companies.Uber;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return the size of the longest non-empty subarray such that the absolute difference
 * between any two elements of this subarray is less than or equal to limit.
 */
public class LongestContinuousSubarrayWithAbsoluteDiffLessEqualtoLimit {
    public int longestSubarray(int[] nums, int limit) {
        Deque<Integer> maxs = new ArrayDeque<>();
        Deque<Integer> mins = new ArrayDeque<>();
        int l = 0, r = 0, re = 0;
        while (r < nums.length) {
            while (!maxs.isEmpty() && nums[r] > maxs.peekLast()) {
                maxs.pollLast();
            }
            while (!mins.isEmpty() && nums[r] < mins.peekLast()) {
                mins.pollLast();
            }
            maxs.add(nums[r]);
            mins.add(nums[r]);
            if (maxs.peek() - mins.peek() > limit) {
                if (maxs.peek() == nums[l]) {
                    maxs.poll();
                }
                if (mins.peek() == nums[l]) {
                    mins.poll();
                }
                l++;
            }
            re = Math.max(re, r-l+1);
            r++;
        }
        return re;
    }

    public int longestSubarrayII(int[] nums, int limit) {
        int i = 0, j;
        TreeMap<Integer, Integer> m = new TreeMap<>();
        for (j = 0; j < nums.length; j++) {
            m.put(nums[j], 1 + m.getOrDefault(nums[j], 0));
            if (m.lastEntry().getKey() - m.firstEntry().getKey() > limit) {
                m.put(nums[i], m.get(nums[i]) - 1);
                if (m.get(nums[i]) == 0) {
                    m.remove(nums[i]);
                }
                i++;
            }
        }
        return j - i;
    }

    @Test
    void test() {
        assertEquals(2, longestSubarray(new int[]{8,2,4,7}, 4));
        assertEquals(4, longestSubarray(new int[]{10,1,2,4,7,2}, 5));
        assertEquals(3, longestSubarray(new int[]{4,2,2,2,4,4,2,2}, 0));
    }
}
