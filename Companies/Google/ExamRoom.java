package Companies.Google;

import java.util.TreeSet;

public class ExamRoom {
    TreeSet<Integer> s;
    int n;

    public ExamRoom(int N) {
        n = N;
        s = new TreeSet<>();
    }

    public int seat() {
        if (s.size() == 0) {
            s.add(0);
            return 0;
        } else {
            int dist = s.first();
            Integer prev = null;
            int student = 0;
            for (Integer i : s) {
                if (prev != null) {
                    int mid = (i-prev)/2;
                    if (mid > dist) {
                        dist = mid;
                        student = prev+dist;
                    }
                }
                prev = i;
            }
            if (n-1-s.last() > dist) {
                student = n-1;
            }
            s.add(student);
            return student;
        }
    }

    public void leave(int p) {
        s.remove(p);
    }
}
