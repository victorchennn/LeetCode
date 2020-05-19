package Topics.Design;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MonitorStation {
    private Map<Car, TreeMap<Integer, Integer>> mon; // <type, <time, number>>

    public MonitorStation() {
        mon = new HashMap<>();
    }

    public void pass(Car type, int time) {
        if (!mon.containsKey(type)) {
            mon.put(type, new TreeMap<>());
            mon.get(type).put(time, 1);
        } else {
            mon.get(type).put(time, mon.get(type).lastEntry().getValue()+1);
        }
    }

    public int check(Car type, int start, int end) {
        if (!mon.containsKey(type)) {
            return 0;
        }
        return mon.get(type).ceilingEntry(start).getValue() -
                mon.get(type).floorEntry(end).getValue();
    }

    enum Car {
        SEDAN,
        SUV,
        TRUCK
    }
}
