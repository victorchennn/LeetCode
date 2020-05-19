package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinimumAreaRectangleII {
    public double minAreaFreeRect(int[][] points) {
        if (points.length < 4) {
            return 0;
        }
        Map<String, List<int[]>> m = new HashMap<>();
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                double cx = (points[i][0] + points[j][0]) / 2.0; // Be careful, divide 2.0 not 2
                double cy = (points[i][1] + points[j][1]) / 2.0;
                double dist = Math.pow((points[i][0] - points[j][0]), 2) + Math.pow((points[i][1] - points[j][1]), 2);
                String key = dist + ":" + cx + ":" + cy; // dist + central coordinate (cx,cy)
                m.computeIfAbsent(key, k->new ArrayList<>()).add(new int[]{i, j});
            }
        }
        double re = Double.MAX_VALUE;
        for (List<int[]> l : m.values()) {
            if (l.size() > 1) {
                for (int i = 0; i < l.size(); i++) {
                    for (int j = i+1; j < l.size(); j++) {
                        int[] p1 = points[l.get(i)[0]];
                        int[] p2 = points[l.get(i)[1]];
                        int[] p3 = points[l.get(j)[0]];
                        double len1 = Math.sqrt(Math.pow(p1[0]-p3[0], 2) + Math.pow(p1[1]-p3[1], 2));
                        double len2 = Math.sqrt(Math.pow(p2[0]-p3[0], 2) + Math.pow(p2[1]-p3[1], 2));
                        re = Math.min(re, len1*len2);
                    }
                }
            }
        }
        return re == Double.MAX_VALUE? 0:re;
    }
}
