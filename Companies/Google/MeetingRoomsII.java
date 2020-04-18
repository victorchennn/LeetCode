package Companies.Google;

import java.util.Arrays;

public class MeetingRoomsII {
    public int minMeetingRooms(int[][] intervals) {
        int len = intervals.length;
        int[] begins = new int[len];
        int[] ends = new int[len];
        for (int i = 0; i < len; i++) {
            begins[i] = intervals[i][0];
            ends[i] = intervals[i][1];
        }
        Arrays.sort(begins);
        Arrays.sort(ends);
        int room = 0, endstart = 0;
        for (int i = 0; i < len; i++) {
            if (begins[i] < ends[endstart]) {  // check if last meeting room has ended.
                room++;
            } else {
                endstart++;
            }
        }
        return room;
    }
}
