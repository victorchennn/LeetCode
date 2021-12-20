package Companies.Uber;

import org.junit.jupiter.api.Test;

import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IPO {
    /**
     * Time Complexity: O(NlogN)
     */
    public int findMaximizedCapital(int k, int w, int[] profits, int[] capital) {
        PriorityQueue<int[]> caps = new PriorityQueue<>((a,b)->a[0]-b[0]);
        PriorityQueue<int[]> prfs = new PriorityQueue<>((a,b)->b[1]-a[1]);
        for (int i = 0; i < profits.length; i++) {
            caps.add(new int[]{capital[i], profits[i]});
        }
        for (int i = 0; i < k; i++) {
            while (!caps.isEmpty() && caps.peek()[0] <= w) {
                prfs.add(caps.poll());
            }
            if (prfs.isEmpty()) {
                break;
            }
            w += prfs.poll()[1];
        }
        return w;
    }

    @Test
    void test() {
        assertEquals(4, findMaximizedCapital(2, 0, new int[]{1, 2, 3}, new int[]{0, 1, 1}));
        assertEquals(6, findMaximizedCapital(3, 0, new int[]{1, 2, 3}, new int[]{0, 1, 2}));
    }
}
