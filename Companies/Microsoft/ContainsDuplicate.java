package Companies.Microsoft;

import java.util.HashMap;
import java.util.Map;

public class ContainsDuplicate {
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if (k < 1 || t < 0) {
            return false;
        }
        Map<Long, Long> m = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            long num = (long)nums[i] - Integer.MIN_VALUE;
            long bucket = num / ((long)t+1);
            if (m.containsKey(bucket) || (m.containsKey(bucket-1) && num - m.get(bucket-1) <= t)
                    || (m.containsKey(bucket+1) && m.get(bucket+1) - num <= t)) {
                return true;
            }
            if (m.size() >= k) {
                long last = ((long)nums[i-k] - Integer.MIN_VALUE) / ((long)t+1);
                m.remove(last);
            }
            m.put(bucket, num);
        }
        return false;
    }
}
