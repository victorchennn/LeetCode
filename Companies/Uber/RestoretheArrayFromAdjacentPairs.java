package Companies.Uber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestoretheArrayFromAdjacentPairs {
    public int[] restoreArray(int[][] adjacentPairs) {
        int len = adjacentPairs.length;
        int[] re = new int[len+1];
        Map<Integer, List<Integer>> m = new HashMap<>();
        for (int[] pair : adjacentPairs) {
            m.computeIfAbsent(pair[0], k-> new ArrayList<>()).add(pair[1]);
            m.computeIfAbsent(pair[1], k-> new ArrayList<>()).add(pair[0]);
        }
        int num = 0, i = 0;
        for (int x : m.keySet()) {
            if (m.get(x).size() == 1) {
                num = x;
                break;
            }
        }
        re[i++] = num;
        while (i < len+1) {
            for (int val : m.get(num)) {
                if (i == 1 || val != re[i-2]) {
                    num = val;
                    break;
                }
            }
            re[i++] = num;
        }
        return re;
    }
}
