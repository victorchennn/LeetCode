package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class MissingRanges {
    public List<String> findMissingRanges(int[] nums, int lower, int upper) {
        List<String> l = new ArrayList<>();
        for (int num : nums) {
            if (num > lower) {
                l.add(lower + ((num-1)>lower?"->"+(num-1):""));
            }
            if (num == upper) {
                return l;
            }
            lower = num+1;
        }
        if (lower <= upper) {
            l.add(lower + (upper>lower?"->"+upper:""));
        }
        return l;
    }
}
