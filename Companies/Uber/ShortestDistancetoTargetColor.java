package Companies.Uber;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * You are given an array colors, in which there are three colors: 1, 2 and 3.
 *
 * You are also given some queries. Each query consists of two integers i and c,
 * return the shortest distance between the given index i and the target color c.
 * If there is no solution return -1.
 */
public class ShortestDistancetoTargetColor {
    public List<Integer> shortestDistanceColor(int[] colors, int[][] queries) {
        int len = colors.length;
        int[][] distances = new int[4][len]; // row1 -> color1, row2 -> color2 ... leave row0 empty
        for (int r = 1; r < 4; r++) {
            Arrays.fill(distances[r], Integer.MAX_VALUE);
        }
        // from left to right
        for (int c = 0; c < len; c++) {
            int color = colors[c];
            for (int r = 1; r < 4; r++) {
                if (r == color) {
                    distances[r][c] = 0;
                } else if (c >= 1 && distances[r][c-1] != Integer.MAX_VALUE) {
                    distances[r][c] = distances[r][c-1]+1;
                }
            }
        }
        // from right to left
        for (int c = len-1; c >= 0; c--) {
            int color = colors[c];
            for (int r = 1; r < 4; r++) {
                if (r == color) {
                    continue;
                }
                if (c < len-1 && distances[r][c+1] != Integer.MAX_VALUE) {
                    distances[r][c] = Math.min(distances[r][c], distances[r][c+1]+1);
                }
            }
        }
        List<Integer> re = new ArrayList<>();
        for (int[] query : queries) {
            if (distances[query[1]][query[0]] == Integer.MAX_VALUE) {
                re.add(-1);
            } else {
                re.add(distances[query[1]][query[0]]);
            }
        }
        return re;
    }
}
