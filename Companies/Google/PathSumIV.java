package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class PathSumIV {

    Map<Integer, Integer> m;
    int re = 0;

    public int pathSum(int[] nums) {
        m = new HashMap<>();
        for (int i : nums) {
            m.put(i/10, i%10);
        }
        helper(nums[0]/10, 0);
        return re;
    }

    private void helper(int num, int sum) {
        if (!m.containsKey(num)) {
            return;
        }
        sum += m.get(num);
        int layer = num/10, po = num%10;
        int left = (layer+1)*10 + po*2-1;
        int right = left+1;
        if (!m.containsKey(left) && !m.containsKey(right)) {
            re += sum;
        } else {
            helper(left, sum);
            helper(right, sum);
        }
    }
}
