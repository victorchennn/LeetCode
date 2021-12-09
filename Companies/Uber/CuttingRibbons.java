package Companies.Uber;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CuttingRibbons {
    public int maxLength(int[] ribbons, int k) {
        int min = 1, max = Integer.MIN_VALUE;
        for (int ribbon : ribbons) {
            max = Math.max(max, ribbon);
        }
        while (min <= max) {
            int mid = min + (max-min)/2;
            if (isValidCuts(ribbons, k, mid)) {
                min = mid+1;
            } else {
                max = mid-1;
            }
        }
        return max;
    }

    private boolean isValidCuts(int[] ribbons, int k, int length) {
        int count = 0;
        for (int ribbon : ribbons) {
            count += ribbon / length;
        }
        return count >= k;
    }

    @Test
    void test() {
        assertEquals(5, maxLength(new int[]{9,7,5}, 3));
        assertEquals(4, maxLength(new int[]{7,5,9}, 4));
        assertEquals(0, maxLength(new int[]{7,5,9}, 22));
    }
}
