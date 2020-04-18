package Companies.Microsoft;

import java.util.*;

public class SkylineProblem {
    public List<List<Integer>> getSkyline(int[][] buildings) {
        List<int[]> l = new ArrayList<>();
        for (int[] b : buildings) {
            l.add(new int[]{b[0], -b[2]});
            l.add(new int[]{b[1], b[2]});
        }
        Collections.sort(l, new Comparator<int[]>(){
            @Override
            public int compare(int[]a, int[] b) {
                if (a[0] == b[0]) {
                    return a[1] - b[1];
                } else {
                    return a[0] - b[0];
                }
            }
        });
        TreeMap<Integer, Integer> m = new TreeMap<>(); // record height times
        m.put(0, 1);
        int prev = 0;
        List<List<Integer>> re = new ArrayList<>();
        for (int[] b : l) {
            if (b[1] < 0) {
                m.put(-b[1], m.getOrDefault(-b[1], 0)+1);
            } else {
                if (m.get(b[1]) > 1) { // if height already exists, need to remove this height or minus 1
                    m.put(b[1], m.get(b[1])-1);
                } else {
                    m.remove(b[1]);
                }
            }
            int cur = m.lastKey();
            if (prev != cur) {
                List<Integer> temp = new ArrayList<>();
                temp.add(b[0]);
                temp.add(cur);
                re.add(temp);
                prev = cur;
            }
        }
        return re;
    }
}
