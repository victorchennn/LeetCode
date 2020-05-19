package Companies.Google;

import javafx.util.Pair;

import java.util.*;

public class TimeMap {

    Map<String, List<Pair<Integer, String>>> m;
    Map<String, TreeMap<Integer, String>> M;
    boolean find;

    public TimeMap() {
        m = new HashMap<>();
        M = new HashMap<>();
        find = false;
    }

    public void set(String key, String value, int timestamp) {
        if (!m.containsKey(key)) {
            m.put(key, new ArrayList<>());
        }
        m.get(key).add(new Pair<>(timestamp, value));
    }

    public String get(String key, int timestamp) {
        if (!m.containsKey(key)) {
            return "";
        }
        List<Pair<Integer, String>> l = m.get(key);
//        int i = Collections.binarySearch(l, new Pair<>(timestamp, ""), (a,b)->a.getKey()-b.getKey()); // !
//        if (i >= 0) {
//            return l.get(i).getValue();
//        } else if (i == -1) {
//            return "";
//        } else {
//            return l.get(-i-2).getValue();  // Be careful -i-1-1!!!!
//        }
        int i = search(l, timestamp);
        if (find) {
            find = false;
            return l.get(i).getValue();
        }

        else if (i == 0)
            return "";
        else
            return l.get(i-1).getValue();
//        TreeMap<Integer, String> tm = M.get(key);
//        Integer i = tm.floorKey(timestamp);
//        return i == null? "":tm.get(i);
    }

    private int search(List<Pair<Integer, String>> list, int num) {
        int l = 0, r = list.size();
        while (l < r) {
            int mid = (l+r)/2;
            if (list.get(mid).getKey() == num) {
                find = true;
                return mid;
            }
            if (list.get(mid).getKey() < num) {
                l = mid+1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public static void main(String...args) {
        TimeMap map = new TimeMap();
        map.set("foo", "bar", 1);
        map.set("foo", "bar2", 4);
        System.out.println(map.get("foo", 1));
        System.out.println(map.get("foo", 5));
    }
}
