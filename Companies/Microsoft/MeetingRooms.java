package Companies.Microsoft;

import java.util.Arrays;

public class MeetingRooms {
    public boolean canAttendMeetings(int[][] intervals) {
        Arrays.sort(intervals, (a, b)->a[0]-b[0]);
        for (int i = 0; i < intervals.length-1; i++) {
            if (intervals[i][1] > intervals[i+1][0]) {
                return false;
            }
        }
        return true;
    }

    public int minMeetingRooms(int[][] intervals) {
        int[] a1 = new int[intervals.length];
        int[] a2 = new int[intervals.length];
        for (int i = 0; i < intervals.length; i++) {
            a1[i] = intervals[i][0];
            a2[i] = intervals[i][1];
        }
        Arrays.sort(a1);
        Arrays.sort(a2);
        int room = 0, index = 0;
        for (int i = 0; i < intervals.length; i++) {
            if (a1[i] < a2[index]) {
                room++;
            } else {
                index++;
            }
        }
        return room;
    }
}
