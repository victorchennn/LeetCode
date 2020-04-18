package Companies.Bloomberg;

import java.util.Arrays;

public class NonoverlappingIntervals {
    public int eraseOverlapIntervals(int[][] intervals) {
        Arrays.sort(intervals, (a, b)->a[1]-b[1]);
        int max = 0, lastend = Integer.MIN_VALUE;
        for (int[] inv : intervals) {
            if (lastend <= inv[0]) {
                max++;
                lastend = inv[1];
            }
        }
        return intervals.length-max;
    }
}
