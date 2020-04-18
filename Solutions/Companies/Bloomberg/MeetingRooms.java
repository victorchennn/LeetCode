package Companies.Bloomberg;

import java.util.Arrays;
import java.util.PriorityQueue;

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

    public int minMeetingRoomsII(int[][] intervals) {
        if (intervals == null || intervals.length == 0)
            return 0;

        // Sort the intervals by start time
        Arrays.sort(intervals, (a,b)->a[0]-b[0]);

        // Use a min heap to track the minimum end time of merged intervals
        PriorityQueue<int[]> heap = new PriorityQueue<int[]>((a,b)->a[1]-b[1]);

        // start with the first meeting, put it to a meeting room
        heap.offer(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            // get the meeting room that finishes earliest
            int[] interval = heap.poll();

            if (intervals[i][0] >= interval[1]) {
                // if the current meeting starts right after
                // there's no need for a new room, merge the interval
                interval[1] = intervals[i][1];
            } else {
                // otherwise, this meeting needs a new room
                heap.offer(intervals[i]);
            }

            // don't forget to put the meeting room back
            heap.offer(interval);
        }

        return heap.size();
    }
}
