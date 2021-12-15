package Companies.Google;

import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MinimumNumberofRefuelingStops {
    public int minRefuelStops(int target, int startFuel, int[][] stations) {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Collections.reverseOrder());
        int stops = 0, prev = 0;
        for (int[] station : stations) {
            int pos = station[0], fuel = station[1];
            startFuel -= pos-prev;
            while (!pq.isEmpty() && startFuel < 0) {
                startFuel += pq.poll();
                stops++;
            }
            if (startFuel < 0) {
                return -1;
            }
            pq.offer(fuel);
            prev = pos;
        }
        startFuel -= target-prev;
        while (!pq.isEmpty() && startFuel < 0) {
            startFuel += pq.poll();
            stops++;
        }
        return startFuel<0? -1:stops;
    }

    @Test
    void test() {
        assertEquals(0, minRefuelStops(1,1, new int[][]{}));
        assertEquals(-1, minRefuelStops(100,1, new int[][]{{10,100}}));
        assertEquals(2, minRefuelStops(100,10, new int[][]{{10,60},{20,30},{30,30},{60,40}}));
    }
}
