package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class ContiguousArray {
    public int findMaxLength(int[] nums) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, -1);
        int maxlen = 0, sum = 0;
        for (int i = 0; i < nums.length; i++) {
            sum = sum + (nums[i] == 1 ? 1 : -1);
            if (map.containsKey(sum)) {
                maxlen = Math.max(maxlen, i - map.get(sum));
            } else {
                map.put(sum, i);
            }
        }
        return maxlen;
    }
}
