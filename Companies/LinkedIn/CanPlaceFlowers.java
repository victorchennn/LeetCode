package Companies.LinkedIn;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * flowers cannot be planted in adjacent plots.
 */
public class CanPlaceFlowers {
    public boolean canPlaceFlowers(int[] flowerbed, int n) {
        int ava = 0;
        for (int i = 0; i < flowerbed.length; i++) {
            if (flowerbed[i] == 1) {
                continue;
            }
            int prev = i == 0? 0 : flowerbed[i-1];
            int next = i == flowerbed.length-1? 0 : flowerbed[i+1];
            if (prev == 0 && next == 0) {
                flowerbed[i] = 1;
                ava++;
            }
            if (ava >= n) {
                return true;
            }
        }
        return ava >= n;
    }

    @Test
    void test() {
        assertEquals(true, canPlaceFlowers(new int[]{1,0,0,0,1}, 1));
        assertEquals(false, canPlaceFlowers(new int[]{1,0,0,0,1}, 2));
        assertEquals(true, canPlaceFlowers(new int[]{1}, 0));
        assertEquals(true, canPlaceFlowers(new int[]{1,0,1,0,1,0}, 0));
    }
}
