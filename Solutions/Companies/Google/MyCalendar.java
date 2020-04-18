package Companies.Google;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class MyCalendar {

    TreeMap<Integer, Integer> m;
    List<int[]> overlap;
    List<int[]> calendar;

    public MyCalendar() {
        m = new TreeMap<>();
        overlap = new ArrayList<>();
        calendar = new ArrayList<>();
    }

    public boolean book(int start, int end) {
        Integer key1 = m.floorKey(start);
        if (key1 != null && m.get(key1) > start) {
            return false;
        }
        Integer key2 = m.ceilingKey(start);
        if (key2 != null && key2 < end) {
            return false;
        }
        m.put(start, end);
        return true;
    }

    public boolean book_better(int start, int end) {
        for (int[] o : calendar) {
            if (o[0] < end && o[1] > start) {
                return false;
            }
        }
        calendar.add(new int[]{start, end});
        return true;
    }

    /* Triple booking */
    public boolean bookII(int start, int end) {
        m.put(start, m.getOrDefault(start, 0)+1);
        m.put(end, m.getOrDefault(end, 0)-1);
        int count = 0;
        for (int i : m.values()) {
            count += i;
            if (count > 2) {
                m.put(start, m.get(start)-1);
                m.put(end, m.get(end)+1);
                return false;
            }
        }
        return true;
    }

    public boolean bookII_better(int start, int end) {
        for (int[] o : overlap) {
            if (o[0] < end && o[1] > start) {
                return false;
            }
        }
        for (int[] o : calendar) {
            if (o[0] < end && o[1] > start) {
                overlap.add(new int[]{Math.max(o[0], start), Math.min(o[1], end)});
            }
        }
        calendar.add(new int[]{start, end});
        return true;
    }

    /* K booking */
    public int bookIII(int start, int end) {
        m.put(start, m.getOrDefault(start, 0)+1);
        m.put(end, m.getOrDefault(end, 0)-1);
        int count = 0, temp = 0;
        for (int i : m.values()) {
            temp += i;
            count = Math.max(count, temp);
        }
        return count;
    }
}
