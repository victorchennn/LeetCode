package Topics.Design;

import java.util.*;

public class LogSystem {
    private final String MIN = "2000:01:01:00:00:00";
    private final String MAX = "2017:12:31:23:59:59";
    private TreeMap<String, LinkedList<Integer>> logs;
    private HashMap<String, Integer> m;

    public LogSystem() {
        logs = new TreeMap<>();
        m = new HashMap<>();
        m.put("Year", 4);
        m.put("Month", 7);
        m.put("Day", 10);
        m.put("Hour", 13);
        m.put("Minute", 16);
        m.put("Second", 19);
    }

    public void put(int id, String timestamp) {
        if (!logs.containsKey(timestamp)) {
            logs.put(timestamp, new LinkedList<>());
        }
        logs.get(timestamp).add(id);
    }

    public List<Integer> retrieve(String s, String e, String gra) {
        int index = m.get(gra);
        String start = s.substring(0, index) + MIN.substring(index);
        String end = e.substring(0, index) + MAX.substring(index);
        Map<String, LinkedList<Integer>> sub = logs.subMap(start, true, end, true);
        List<Integer> re = new ArrayList<>();
        for (Map.Entry<String, LinkedList<Integer>> entry : sub.entrySet()) {
            re.addAll(entry.getValue());
        }
        return re;
    }
}
