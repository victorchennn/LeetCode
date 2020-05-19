package Companies.Google;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FrogJump {
    public boolean canCross(int[] stones) {
        Map<Integer, Set<Integer>> m = new HashMap<>();
        for (int i : stones) {
            m.put(i, new HashSet<>());
        }
        m.get(0).add(0);
        for (int stone : stones) {
            for (int step : m.get(stone)) {
                for (int i = step-1; i <= step+1; i++) {
                    if (i > 0 && m.containsKey(stone+i)) {
                        m.get(stone+i).add(i);
                    }
                }
            }
        }
        return m.get(stones[stones.length-1]).size() > 0;
    }
}
