package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CapacityToShipPackagesWithinDDays {
    public int shipWithinDays(int[] weights, int D) {
        int l = 0, r = 0;
        for (int w : weights) {
            r += w;
            l = Math.max(l, w);
        }
        while (l < r) {
            int mid = l + (r - l) / 2;
            int sum = 0, day = 1;
            for (int w : weights) {
                if (sum + w > mid) {
                    day++;
                    sum = 0;
                }
                sum += w;
            }
            if (day > D) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    @Test
    void test() {
        assertEquals(15, shipWithinDays(new int[]{1,2,3,4,5,6,7,8,9,10}, 5));
        assertEquals(6, shipWithinDays(new int[]{3,2,2,4,1,4}, 3));
        assertEquals(3, shipWithinDays(new int[]{1,2,3,1,1,}, 4));
    }
}
