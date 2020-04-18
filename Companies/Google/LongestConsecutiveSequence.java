package Companies.Google;

import java.util.HashSet;
import java.util.Set;

public class LongestConsecutiveSequence {
    public int longestConsecutive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int i : nums) {
            set.add(i);
        }
        int len = 0; // might be empty array
        for (int i : set) {
            if (!set.contains(i-1)) { // start from lowest
                int temp = 1;
                while (set.contains(i+1)) {
                    temp++;
                    i++;
                }
                len = Math.max(len, temp);
            }
        }
        return len;
    }
}
