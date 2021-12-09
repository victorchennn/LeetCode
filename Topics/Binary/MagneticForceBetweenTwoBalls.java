package Topics.Binary;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MagneticForceBetweenTwoBalls {
    public int maxDistance(int[] position, int m) {
        int len = position.length;
        Arrays.sort(position);
        int l = 0, r = position[len-1] - position[0];
        while (l < r) {
            int mid = l + (r-l)/2;
            if (count(mid, position) >= m) {
                l = mid;
            } else {
                r = mid-1;
            }
        }
        return l;
    }

    private int count(int dis, int[] position) {
        int cur = position[0], re = 1;
        for (int i = 1; i < position.length; i++) {
            if (position[i] - cur >= dis) {
                re++;
                cur = position[i];
            }
        }
        return re;
    }

    @Test
    void test() {
        assertEquals(3, maxDistance(new int[]{1,2,3,4,7}, 3));
    }
}
