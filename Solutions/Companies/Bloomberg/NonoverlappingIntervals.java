package Companies.Bloomberg;

import java.util.Arrays;

/**
 * @see AvailableTime
 * @see EmployeeFreeTime
 * @see MaximumLengthofPairChain
 * @see MeetingRooms
 * @see MergeIntervals
 * @see RemoveArrayElementsinGivenIndexRanges
 */
public class NonoverlappingIntervals {
    public int eraseOverlapIntervals(int[][] intervals) {
        int count = 0;
        if(intervals == null || intervals.length == 0) {
            return count;
        }
        Arrays.sort(intervals,(a,b)->Integer.compare(a[0],b[0]));
        int[] prev = intervals[0];
        for(int i = 1; i < intervals.length; i++){
            if(intervals[i][0] < prev[1]){
                count++;
                prev[1] =  Math.min(prev[1], intervals[i][1]);
            } else {
                prev = intervals[i];
            }
        }
        return count;
    }

    public int eraseOverlapIntervalsII(int[][] intervals) {
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
