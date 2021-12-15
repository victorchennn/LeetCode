package Topics.Design;

import java.util.TreeSet;

public class ExamRoom {
    private TreeSet<Integer> rooms = new TreeSet<>();
    private int n;

    public ExamRoom(int n) {
        this.n = n;
    }

    public int seat() {
        if (rooms.size() == 0) {
            rooms.add(0);
            return 0;
        }
        Integer prev = null;
        int dist = rooms.first();
        int student = 0;
        for (Integer pos : rooms) {
            if (prev == null) {
                prev = pos;
                continue;
            }
            int mid = (pos-prev)/2;
            if (mid > dist) {
                dist = mid;
                student = prev+mid;
            }
            prev = pos;
        }
        if (n-1-rooms.last() > dist) {
            student = n-1;
        }
        rooms.add(student);
        return student;

    }

    public void leave(int p) {
        rooms.remove(p);
    }
}
