package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Map;

public class MaxPointsOnALine {
    public int maxPoints(int[][] points) {
        if (points.length <= 1) {
            return points.length;
        }
        int re = 0;
        for (int i = 0; i < points.length-1; i++) {
            int overlap = 0, maxline = 0;
            Map<String, Integer> m = new HashMap<>();
            for (int j = i+1; j < points.length; j++) {
                int dx = points[j][0] - points[i][0];
                int dy = points[j][1] - points[i][1];
                if (dx == 0 && dy == 0) {
                    overlap++;
                    continue;
                }
                int gcd = gcd(dx, dy);
                dx /= gcd;
                dy /= gcd;
                String slope = dy + "/" + dx;
                m.put(slope, m.getOrDefault(slope, 0)+1);
                maxline = Math.max(maxline, m.get(slope));
            }
            re = Math.max(re, 1 + overlap + maxline);
        }
        return re;
    }

    private int gcd(int x, int y) {
        if (y == 0) {
            return x;
        }
        return gcd(y, x%y);
    }
}
