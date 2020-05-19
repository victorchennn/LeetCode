package Companies.Twitter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see Companies.Bloomberg.AvailableTime
 * @see Companies.Bloomberg.EmployeeFreeTime
 * @see Companies.Bloomberg.MaximumLengthofPairChain
 * @see Companies.Bloomberg.MeetingRooms
 * @see Companies.Bloomberg.NonoverlappingIntervals
 * @see Companies.Bloomberg.RemoveArrayElementsinGivenIndexRanges
 */
public class MergeIntervals {
    public int[][] merge(int[][] intervals) {
        if (intervals == null || intervals.length <= 1) {
            return intervals;
        }
        Arrays.sort(intervals, (a, b)->a[0]-b[0]);
        List<int[]> l = new ArrayList<>();
        int[] cur = intervals[0];
        l.add(cur);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] > cur[1]) {
                cur = intervals[i];
                l.add(cur);
            } else {
                cur[1] = Math.max(cur[1], intervals[i][1]);
            }
        }
        return l.toArray(new int[l.size()][2]);
    }
}
