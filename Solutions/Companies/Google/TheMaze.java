package Companies.Google;

import java.util.LinkedList;
import java.util.Queue;

public class TheMaze {
    public boolean hasPath(int[][] maze, int[] start, int[] destination) {
        int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};
        Queue<int[]> q = new LinkedList<>();
        q.add(start);
        boolean[][] marks = new boolean[maze.length][maze[0].length];
        marks[start[0]][start[1]] = true;
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            if (cur[0] == destination[0] && cur[1] == destination[1]) {
                return true;
            }
            for (int[] dir : dirs) {
                int rr = cur[0] + dir[0];
                int cc = cur[1] + dir[1];
                while (rr >= 0 && cc >= 0 && rr < maze.length && cc < maze[0].length && maze[rr][cc]==0 ) {
                    rr += dir[0];
                    cc += dir[1];
                }
                if (!marks[rr - dir[0]][cc - dir[1]]) {
                    q.add(new int[]{rr - dir[0], cc - dir[1]});
                    marks[rr - dir[0]][cc - dir[1]] = true;
                }
            }
        }
        return false;
    }
}
