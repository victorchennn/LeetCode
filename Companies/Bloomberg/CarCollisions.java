package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CarCollisions {
    public int collisions(int[] nums) {
        int countZero = 0, sum = 0;
        for (int num : nums) {
            if (num == 0) {
                countZero++;
            } else {
                sum += countZero;
            }
        }
        return sum;
    }

    @Test
    void test() {
        assertEquals(1, collisions(new int[]{0,1}));
        assertEquals(2, collisions(new int[]{0,0,1}));
        assertEquals(1, collisions(new int[]{1,0,0,1}));
        assertEquals(5, collisions(new int[]{0,1,0,1,1,0}));
    }
}
