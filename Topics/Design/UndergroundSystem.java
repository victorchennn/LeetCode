package Topics.Design;

import javafx.util.Pair;

import java.util.HashMap;
import java.util.Map;

public class UndergroundSystem {
    private Map<Integer, Pair<String, Integer>> checkInMap = new HashMap<>();
    private Map<String, Pair<Integer, Integer>> checkOutMap = new HashMap<>();


    /**
     * Space Complexity O(P+S^2),
     * S is the number of stations on the network,
     * P is the number of passengers making a journey concurrently during peak time.
     * Size of CheckInMap O(P)
     * Size of CheckOutMap O(S^2), every possible pair of the stations.
     * */
    public UndergroundSystem() {

    }

    public void checkIn(int id, String stationName, int t) {
        checkInMap.put(id, new Pair<>(stationName, t));
    }

    public void checkOut(int id, String stationName, int t) {
        Pair<String, Integer> in = checkInMap.get(id);
        String route = in.getKey() + "->" + stationName;
        int duration = t - in.getValue();
        Pair<Integer, Integer> out = checkOutMap.getOrDefault(route, new Pair<>(0, 0));
        checkOutMap.put(route, new Pair<>(out.getKey() + duration, out.getValue()+1));
    }

    public double getAverageTime(String startStation, String endStation) {
        String route = startStation + "->" + endStation;
        Pair<Integer, Integer> out = checkOutMap.get(route);
        return (double)out.getKey()/out.getValue();
    }
}
