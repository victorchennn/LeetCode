package Companies.Amazon;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        int re = 0;
        for (int num : nums) {
            if (!set.contains(num-1)) {
                int len = 0;
                while (set.contains(num)) {
                    len++;
                    num++;
                }
                re = Math.max(re, len);
            }
        }
        return re;
    }
}
