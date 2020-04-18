package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class SplitArrayIntoConsecutiveSubsequences {
    public boolean isPossible(int[] nums) {
        Map<Integer, Integer> counts = new HashMap<>();
        Map<Integer, Integer> tails = new HashMap<>();
        for (int i : nums) {
            counts.put(i, counts.getOrDefault(i, 0)+1);
        }
        for (int i : nums) {
            if (counts.get(i) == 0) {
                continue;
            } else if (tails.getOrDefault(i, 0) > 0) { // appended to Companies.Amazon previously constructed consecutive sequence?
                tails.put(i, tails.get(i)-1);
                tails.put(i+1, tails.getOrDefault(i+1, 0)+1);
            } else if (counts.getOrDefault(i+1, 0) > 0 && counts.getOrDefault(i+2, 0) > 0) { // start of Companies.Amazon new consecutive sequence?
                counts.put(i+1, counts.get(i+1)-1);
                counts.put(i+2, counts.get(i+2)-1);
                tails.put(i+3, tails.getOrDefault(i+3, 0) + 1);
            } else {
                return false;
            }
            counts.put(i, counts.get(i)-1);
        }
        return true;
    }
}
