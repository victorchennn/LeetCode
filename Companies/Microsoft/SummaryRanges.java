package Companies.Microsoft;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void test() {
        assertEquals(true, summaryRanges(new int[]{0,1,2,4,5,7}).equals(
                new ArrayList<>(Arrays.asList("0->2","4->5","7"))));
        assertEquals(true, summaryRanges(new int[]{0,2,3,4,6,8,9}).equals(
                new ArrayList<>(Arrays.asList("0","2->4","6","8->9"))));
    }
}
