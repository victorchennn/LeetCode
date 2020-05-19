package Companies.Google;

import java.util.List;

public class KeysAndRooms {
    public boolean canVisitAllRooms(List<List<Integer>> rooms) {
        boolean[] marks = new boolean[rooms.size()];
        dfs(marks, rooms, 0);
        for (boolean m : marks) {
            if (!m) {
                return false;
            }
        }
        return true;
    }

    private void dfs(boolean[] marks, List<List<Integer>> rooms, int s) {
        if (marks[s]) {
            return;
        }
        marks[s] = true;
        for (int r : rooms.get(s)) {
            dfs(marks, rooms, r);
        }
    }
}
