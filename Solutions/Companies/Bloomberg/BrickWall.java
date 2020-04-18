package Companies.Bloomberg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BrickWall {
    public int leastBricks(List<List<Integer>> wall) {
        Map<Integer, Integer> m = new HashMap<>(); // length, count
        int count = 0;
        for (List<Integer> w : wall) {
            int len = 0;
            for (int i = 0; i < w.size()-1; i++) {
                len += w.get(i);
                m.put(len, m.getOrDefault(len, 0)+1);
                count = Math.max(count, m.get(len));
            }
        }
        return wall.size()-count;
    }
}
