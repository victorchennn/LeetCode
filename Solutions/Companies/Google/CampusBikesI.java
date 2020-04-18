package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CampusBikesI {
    public int[] assignBikes(int[][] workers, int[][] bikes) {
        List<int[]>[] l = new ArrayList[2001];
        for (int i = 0; i < workers.length; i++) {
            for (int j = 0; j < bikes.length; j++) {
                int dis = Math.abs(workers[i][0] - bikes[j][0]) + Math.abs(workers[i][1] - bikes[j][1]);
                if (l[dis] == null) {
                    l[dis] = new ArrayList<>();
                }
                l[dis].add(new int[]{i, j});
            }
        }
        int[] re = new int[workers.length];
        boolean[] occupy = new boolean[bikes.length];
        Arrays.fill(re, -1);
        for (int i = 0; i < l.length; i++) {
            if (l[i] != null) {
                List<int[]> cur = l[i];
                for (int[] pair : cur) {
                    int worker = pair[0];
                    int bike = pair[1];
                    if (re[worker] == -1 && !occupy[bike]) {
                        re[worker] = bike;
                        occupy[bike] = true;
                    }
                }
            }
        }
        return re;
    }
}
