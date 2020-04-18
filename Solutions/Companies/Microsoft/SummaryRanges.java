package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class SummaryRanges {
    public List<String> summaryRanges(int[] nums) {
        List<String> l = new ArrayList<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            while (i+1 < nums.length && (nums[i+1] - nums[i] == 1)) {
                i++;
            }
            if (nums[i] != num) {
                l.add(num + "->" + nums[i]);
            } else {
                l.add(num + "");
            }
        }
        return l;
    }
}
