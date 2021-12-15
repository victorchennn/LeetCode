package Companies.Amazon;

import Companies.Bloomberg.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * N is the number of employees
 * C is the number of jobs across all employees
 *
 * @see AvailableTime
 * @see MaximumLengthofPairChain
 * @see MeetingRooms
 * @see MergeIntervals
 * @see NonoverlappingIntervals
 * @see RemoveArrayElementsinGivenIndexRanges
 */
public class EmployeeFreeTime {
    /**
     * Time Complexity: O(ClogC)
     * Space Complexity: O(C)
     */
    public List<Interval> employeeFreeTime(List<List<Interval>> schedule) {
        List<Interval> re = new ArrayList<>();
        if (schedule.size() == 0) {
            return re;
        }
        List<Interval> timeLine = new ArrayList<>();
        for (List<Interval> sch : schedule) {
            timeLine.addAll(sch);
        }
        Collections.sort(timeLine, (a, b)->a.start-b.start);
        Interval prev = timeLine.get(0);
        for (Interval itv : timeLine) {
            if (prev.end < itv.start) {
                re.add(new Interval(prev.end, itv.start));
                prev = itv;
            } else {
                prev = prev.end < itv.end? itv:prev;
            }
        }
        return re;
    }

    /**
     * Time Complexity: O(ClogN)
     * Space Complexity: O(N)
     */
    public List<Interval> employeeFreeTimeII(List<List<Interval>> schedule) {
        List<Interval> re = new ArrayList<>();
        if (schedule.size() == 0) {
            return re;
        }
        PriorityQueue<Job> pq = new PriorityQueue<Job>((a, b) ->
                schedule.get(a.i).get(a.index).start -
                        schedule.get(b.i).get(b.index).start);
        int ei = 0, anchor = Integer.MAX_VALUE;

        for (List<Interval> employee: schedule) {
            pq.offer(new Job(ei++, 0));
            anchor = Math.min(anchor, employee.get(0).start);
        }

        while (!pq.isEmpty()) {
            Job job = pq.poll();
            Interval iv = schedule.get(job.i).get(job.index);
            if (anchor < iv.start)
                re.add(new Interval(anchor, iv.start));
            anchor = Math.max(anchor, iv.end);
            if (++job.index < schedule.get(job.i).size())
                pq.offer(job);
        }

        return re;
    }

    private class Job {
        int i, index; // i is the global index, index is the internal index
        Job(int i, int index) {
            this.i = i;
            this.index = index;
        }
    }

    private class Interval {
        int start, end;
        Interval(int _start, int _end) {
            start = _start;
            end = _end;
        }
    }
}
