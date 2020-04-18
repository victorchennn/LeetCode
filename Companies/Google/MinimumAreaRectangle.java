package Companies.Google;

import java.util.*;

public class MinimumAreaRectangle {
    public int minAreaRect(int[][] points) {
        Set<Integer> s = new HashSet<>();
        for (int[] p : points) {
            s.add(p[0]*40001 + p[1]);
        }
        int min = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                if (points[i][0] != points[j][0] && points[i][1] != points[j][1] &&
                        s.contains(points[i][0]*40001+points[j][1]) &&
                        s.contains(points[j][0]*40001+points[i][1])) {
                    min = Math.min(min, Math.abs((points[i][0]-points[j][0])*(
                            points[i][1]-points[j][1])));
                }
            }
        }
        return min == Integer.MAX_VALUE?0:min;
    }
}
