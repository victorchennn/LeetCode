package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SingleCycle {
    public static boolean ifSingle(int[] nums, int start) {
        int count = 1, index = start+nums[start];
        Set<Integer> s = new HashSet<>();
        s.add(start);
        while (index != start && count != nums.length) {
            if (!s.add(index)) {
                return false;
            }
            index += nums[index];
            count++;
        }
        return index == start && count == nums.length;
    }

    @Test
    void test() {
        assertEquals(true, ifSingle(new int[]{1,2,-2,1,-2}, 0));
        assertEquals(false, ifSingle(new int[]{3,1,-2,-3}, 0));
        assertEquals(false, ifSingle(new int[]{1,1,-1,-3}, 0));
    }
}
