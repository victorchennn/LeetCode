package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class MaximumSizeSubarraySumEqualsK {
    public int maxSubArrayLen(int[] nums, int k) {
        int sum = 0, re = 0;
        Map<Integer, Integer> m = new HashMap<>();
        m.put(0, -1);
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            if (m.containsKey(sum-k)) {
                re = Math.max(re, i-m.get(sum-k));
            }
            m.putIfAbsent(sum, i);
        }
        return re;
    }
}
