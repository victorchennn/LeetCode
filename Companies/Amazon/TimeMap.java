package Companies.Amazon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class TimeMap {

    private Map<String, List<Stamp>> m;
//    private Map<String, TreeMap<Integer, String>> m;

    /** Initialize your data structure here. */
    public TimeMap() {
        m = new HashMap<>();
    }

    public void set(String key, String value, int timestamp) {
        m.computeIfAbsent(key, k->new ArrayList<>()).add(new Stamp(value, timestamp));
//        m.computeIfAbsent(key, k->new TreeMap<>()).put(timestamp, value);
    }

    public String get(String key, int timestamp) {
        if (!m.containsKey(key)) {
            return "";
        }
        return search(m.get(key), timestamp);

//        TreeMap<Integer, String> tm = m.get(key);
//        Integer time = tm.floorKey(timestamp);
//        return time == null? "":tm.get(time);
    }

    private String search(List<Stamp> list, int time) {
        int low = 0, high = list.size()-1;
        while (low < high) {
            int mid = (low+high+1) >> 1;
            if (list.get(mid).time <= time) {
                low = mid;
            } else {
                high = mid-1;
            }
        }
        return list.get(low).time <= time? list.get(low).val:"";
    }

    private class Stamp {
        String val;
        int time;
        Stamp(String val, int time) {
            this.val = val;
            this.time = time;
        }
    }
}

