package Companies.Bloomberg;

import java.util.*;

/**
 * @see EmployeeFreeTime
 * @see MeetingRooms
 * @see MergeIntervals
 * @see NonoverlappingIntervals
 * @see RemoveArrayElementsinGivenIndexRanges
 */
public class AvailableTime {
    public static List<Interval> availableTime(List<Interval> l) {
        if (l == null || l.size() == 0) {
            return new ArrayList<>();
        }
        l.sort((a,b)->compare(a.start, b.start));
        LinkedList<Interval> merge = new LinkedList<>();
        for (Interval itv : l) {
            if (merge.isEmpty() || compare(merge.peekLast().end, itv.start) < 0) {
                merge.add(itv);
            } else if (compare(merge.peekLast().end, itv.end) < 0) {
                merge.peekLast().end = itv.end;
            }
        }
        Time start = new Time(0, 0);
        Time end = new Time(24, 0);
        List<Interval> re = new ArrayList<>();
        for (Interval itv : merge) {
            if (compare(start, itv.start) < 0) {
                re.add(new Interval(start, itv.start));
                start = itv.end;
            }
        }
        if (compare(start, end) < 0) {
            re.add(new Interval(start, end));
        }
        return re;
    }

    private static int compare(Time t1, Time t2) {
        if (t1.hour != t2.hour) {
            return t1.hour - t2.hour;
        } else {
            return t1.minute - t2.minute;
        }
    }

    public static void main(String...args) {
        List<Interval> schedule = new ArrayList<>(Arrays.asList(
                new Interval(new Time(9, 0), new Time(12, 30)),
                new Interval(new Time(8, 30), new Time(9, 30)),
                new Interval(new Time(12, 45), new Time(12, 50))));
        System.out.println(availableTime(schedule));
    }

    static class Interval {
        Time start, end;
        Interval(Time start, Time end) {
            this.start = start;
            this.end = end;
        }
    }

    static class Time {
        int hour, minute;
        Time(int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
        }
    }

}
