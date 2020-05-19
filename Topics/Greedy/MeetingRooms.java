package Topics.Greedy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;

/**
 * @see Companies.Bloomberg.AvailableTime
 * @see EmployeeFreeTime
 * @see Companies.Bloomberg.MaximumLengthofPairChain
 * @see Companies.Bloomberg.MergeIntervals
 * @see NonoverlappingIntervals
 * @see Companies.Bloomberg.RemoveArrayElementsinGivenIndexRanges
 */
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
        Arrays.sort(intervals, (a,b)->a[0]-b[0]);
        PriorityQueue<Integer> q = new PriorityQueue<>(); // endingTime
        q.add(intervals[0][1]);
        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= q.peek()) {
                q.poll();
            }
            q.add(intervals[i][1]);
        }
        return q.size();
    }

    /**
     * @Follow-up: Assign room index to each interval
     */
    public static List<List<int[]>> minMeetingRoomsIII(int[][] intervals) {
        List<List<int[]>> re = new ArrayList<>();
        if (intervals == null || intervals.length == 0)
            return re;
        Arrays.sort(intervals, (a,b)->a[0]-b[0]);
        PriorityQueue<int[]> q = new PriorityQueue<>((a,b)->a[0]-b[0]); // endingTime
        q.add(new int[]{intervals[0][1], 0});
        re.add(new ArrayList<>());
        re.get(0).add(intervals[0]);

        for (int i = 1; i < intervals.length; i++) {
            if (intervals[i][0] >= q.peek()[0]) {
                int[] prev = q.poll();
                re.get(prev[1]).add(intervals[i]);
                q.add(new int[]{intervals[i][1], prev[1]});
            } else {
                re.add(new ArrayList<>());
                re.get(re.size()-1).add(intervals[i]);
                q.add(new int[]{intervals[i][1], re.size()-1});
            }
        }
        return re;
    }

    public static void main(String...args) {
        minMeetingRoomsIII(new int[][]{{7,10},{2,4}});
        minMeetingRoomsIII(new int[][]{{0,30},{5,10},{15,20}});
        minMeetingRoomsIII(new int[][]{{2,15},{36,45},{9,29},{16,23},{4,9}});
        minMeetingRoomsIII(new int[][]{{1,5},{2,6},{3,7},{4,8},{5,9},{6,10},{7,11}});
    }
}
