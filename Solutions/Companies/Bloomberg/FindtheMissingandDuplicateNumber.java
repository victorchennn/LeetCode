package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FindtheMissingandDuplicateNumber {
    /* Warning: Modify the array */
    public int[] find(int[] nums) {
        int missing = -1, duplicate = -1;
        for (int num : nums) {
            int index = Math.abs(num)-1;
            if (nums[index] < 0) {
                duplicate = index+1;
                continue;
            }
            nums[index] = -nums[index];
        }
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] > 0) {
                missing = i+1;
            }
        }
//        System.out.println(duplicate);
//        System.out.println(missing);
        return new int[]{missing, duplicate};
    }

    public int[] findII(int[] nums) {
        int missing = -1, duplicate = -1, sum = 0, truesum = 0;
        Set<Integer> s = new HashSet<>();
        for (int i = 0; i < nums.length; i++) {
            sum += nums[i];
            truesum += i+1;
            if (!s.add(nums[i])) {
                duplicate = nums[i];
            }
        }
        missing = duplicate - (sum - truesum);
//        System.out.println(duplicate);
//        System.out.println(missing);
        return new int[]{missing, duplicate};
    }

    @Test
    void test() {
        assertEquals(true, Arrays.equals(new int[]{2,3}, find(new int[]{3,1,3})));
        assertEquals(true, Arrays.equals(new int[]{5,1}, find(new int[]{4,3,6,2,1,1})));
        assertEquals(true, Arrays.equals(new int[]{2,3}, findII(new int[]{3,1,3})));
        assertEquals(true, Arrays.equals(new int[]{5,1}, findII(new int[]{4,3,6,2,1,1})));
    }
}
