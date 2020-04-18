package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmployeeFreeTime {
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

    class Interval {
        public int start;
        public int end;

        public Interval() {}

        public Interval(int _start, int _end) {
            start = _start;
            end = _end;
        }
    };
}
