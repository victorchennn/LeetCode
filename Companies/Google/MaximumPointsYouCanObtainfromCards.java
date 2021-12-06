package Companies.Google;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MaximumPointsYouCanObtainfromCards {
    public int maxScore(int[] cardPoints, int k) {
        int len = cardPoints.length, lsum = 0, rsum = 0;
        for (int i = 0; i < k; i++) {
            lsum += cardPoints[i];
        }
        int max = lsum;
        for (int i = 0; i < k; i++) {
            rsum += cardPoints[len-1-i];
            lsum -= cardPoints[k-1-i];
            max = Math.max(max, lsum+rsum);
        }
        return max;
    }

    @Test
    void test() {
        assertEquals(12, maxScore(new int[]{1,2,3,4,5,6,1}, 3));
        assertEquals(4, maxScore(new int[]{2,2,2}, 2));
        assertEquals(55, maxScore(new int[]{9,7,7,9,7,7,9},7));
        assertEquals(1, maxScore(new int[]{1,1000,1},1));
        assertEquals(202, maxScore(new int[]{1,79,80,1,1,1,200,1},3));
    }
}
