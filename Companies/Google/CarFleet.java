package Companies.Google;

import java.util.TreeMap;

public class CarFleet {
    public int carFleet(int target, int[] position, int[] speed) {
        TreeMap<Integer, Double> m = new TreeMap<>();
        for (int i = 0; i < position.length; i++) {
            m.put(-position[i], (double)(target-position[i])/speed[i]); // use double
        }
        int re = 0;
        double cur = 0;
        for (double time : m.values()) {
            if (time > cur) {
                cur = time; // change only bigger than first car
                re++;
            }
        }
        return re;
    }
}
