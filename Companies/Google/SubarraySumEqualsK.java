package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class SubarraySumEqualsK {
    public int subarraySum(int[] nums, int k) {
        Map<Integer, Integer> m = new HashMap<>();
        m.put(0, 1);
        int sum = 0, re = 0;
        for (int num : nums) {
            sum += num;
            if (m.containsKey(sum-k)) {
                re += m.get(sum-k);
            }
            m.put(sum, m.getOrDefault(sum, 0)+1);
        }
        return re;
    }
}
