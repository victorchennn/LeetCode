package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class FruitIntoBaskets {
    public int totalFruit(int[] tree) {
        Map<Integer, Integer> m = new HashMap<>();
        int l = 0, re = 0;
        for (int r = 0; r < tree.length; r++) {
            m.put(tree[r], m.getOrDefault(tree[r], 0)+1);
            while (m.size() > 2) {
                m.put(tree[l], m.get(tree[l])-1);
                if (m.get(tree[l]) == 0) {
                    m.remove(tree[l]);
                }
                l++;
            }
            re = Math.max(re, r-l+1);
        }
        return re;
    }
}
