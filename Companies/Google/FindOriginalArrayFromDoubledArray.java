package Companies.Google;

import java.util.*;

/**
 * Given an array changed, return original if changed is a doubled array.
 *
 * Time Complexity: O(N + KlogK)
 *
 * @see ArrayofDoubledPairs
 */
public class FindOriginalArrayFromDoubledArray {
    public int[] findOriginalArray(int[] changed) {
        int len = changed.length, i = 0;
        if (len % 2 != 0) {
            return new int[0];
        }
        int[] re = new int[len/2];
        Map<Integer, Integer> count = new HashMap<>();
        for (int val : changed) {
            count.put(val, count.getOrDefault(val,0)+1);
//            count.merge(val, 1, Integer::sum);
        }
        List<Integer> vals = new ArrayList<>(count.keySet());
        Collections.sort(vals);
        for (int val : vals) {
            if (count.get(val) > count.getOrDefault(val*2, 0)) {
                return new int[0];
            }
            for (int j = 0; j < count.get(val); j++) {
                re[i++] = val;
                count.put(val*2, count.get(val*2)-1);
            }
        }
        return re;
    }
}
