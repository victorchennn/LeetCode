package Companies.LinkedIn;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * return the number of triplets chosen from the array that
 * can make triangles if we take them as side lengths of a triangle.
 */
public class ValidTriangleNumber {
    public int triangleNumber(int[] nums) {
        if(nums.length < 3){
            return 0;
        }
        Arrays.sort(nums);
        int count = 0;
        for (int i = nums.length-1; i >= 2; i--) {
            int l = 0, r = i-1;
            while (l < r) {
                if (nums[l] + nums[r] > nums[i]) {
                    count += r-l;
                    r--;
                } else {
                    l++;
                }
            }
        }
        return count;
    }

    @Test
    void test() {
        assertEquals(3, triangleNumber(new int[]{2,2,3,4}));
        assertEquals(4, triangleNumber(new int[]{4,2,3,4}));
    }
}
