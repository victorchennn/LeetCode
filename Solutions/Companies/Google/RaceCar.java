package Companies.Google;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class RaceCar {
    public int racecar(int target) {
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{0, 1});
        Set<String> s = new HashSet<>();
        s.add(0+":"+1);
        for (int re = 0; !q.isEmpty(); re++) {
            for (int i = q.size(); i > 0; i--) {
                int[] cur = q.poll();
                if (cur[0] == target) {
                    return re;
                }
                int[] a = new int[]{cur[0]+cur[1], cur[1]*2}; // accelerate
                String key = a[0] + ":" + a[1];
                if (!s.contains(key) && a[0] > 0 && a[0] < target*2) {
                    q.add(a);
                    s.add(key);
                }
                int[] r = new int[]{cur[0], cur[1]>0? -1:1}; // stay
                key = r[0] + ":" + r[1];
                if (!s.contains(key) && r[0] > 0 && r[0] < target*2) {
                    q.add(r);
                    s.add(key);
                }
            }
        }
        return -1;
    }
}
