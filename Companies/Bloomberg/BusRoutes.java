package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Return the least number of buses you must take to travel from
 * source to target. Return -1 if it is not possible.
 */
public class BusRoutes {
    public int numBusesToDestination(int[][] routes, int source, int target) {
        Map<Integer, Set<Integer>> map = new HashMap<>();
        for (int i = 0; i < routes.length; i++) {
            for (int route : routes[i]) {
                map.computeIfAbsent(route, k->new HashSet<>()).add(i);
            }
        }
        Queue<int[]> q = new ArrayDeque<>();
        q.add(new int[]{source, 0});
        Set<Integer> seen = new HashSet<>();
        seen.add(source);
        boolean[] marked = new boolean[routes.length];
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            int stop = cur[0], buses = cur[1];
            if (stop == target) {
                return buses;
            }
            for (int route : map.get(stop)) {
                if (marked[route]) {
                    continue;
                }
                for (int s : routes[route]) {
                    if (!seen.contains(s)) {
                        seen.add(s);
                        q.add(new int[]{s, buses+1});
                    }
                }
                marked[route] = true;
            }
        }
        return -1;
    }

    @Test
    void test() {
        assertEquals(2, numBusesToDestination(new int[][]{{1,2,7},{3,6,7}},1, 6));
    }
}

