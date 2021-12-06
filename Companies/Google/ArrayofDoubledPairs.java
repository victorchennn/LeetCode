package Companies.Google;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given an integer array of even length arr, return true if it is possible
 * to reorder arr such that arr[2 * i + 1] = 2 * arr[2 * i]
 * for every 0 <= i < len(arr) / 2, or false otherwise.
 *
 * Time Complexity: O(NlogK)
 *
 * @see FindOriginalArrayFromDoubledArray
 */
public class ArrayofDoubledPairs {
    public boolean canReorderDoubled(int[] arr) {
        Map<Integer, Integer> count = new TreeMap<>();
        int sum = 0;
        for (int val : arr) {
            sum += val;
            count.put(val, count.getOrDefault(val, 0)+1);
        }
        if (sum%3 != 0) {
            return false;
        }
        for (int val : count.keySet()) {
            if (count.get(val) == 0) {
                continue;
            }
            int want = val < 0? val/2 : val*2;
            if (val < 0 && val%2 != 0 || count.get(val) > count.getOrDefault(want, 0)) {
                return false;
            }
            count.put(want, count.get(want)-count.get(val));
        }
        return true;
    }

    @Test
    void test() {
        assertEquals(false, canReorderDoubled(new int[]{3,1,3,6}));
        assertEquals(false, canReorderDoubled(new int[]{2,1,2,6}));
        assertEquals(true, canReorderDoubled(new int[]{4,-2,2,-4}));
        assertEquals(false, canReorderDoubled(new int[]{1,2,4,16,8,4}));
        assertEquals(false, canReorderDoubled(new int[]{-5,-2}));
    }
}
