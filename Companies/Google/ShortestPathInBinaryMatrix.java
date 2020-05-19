package Companies.Google;

import java.util.LinkedList;
import java.util.Queue;

public class ShortestPathInBinaryMatrix {
    public int shortestPathBinaryMatrix(int[][] grid) {
        if (grid[0][0] == 1) {
            return -1;
        }
        int[][] dirs = new int[][]{{0,1},{0,-1},{1,1},{1,0},{1,-1},{-1,1},{-1,0},{-1,-1}};
        Queue<int[]> q = new LinkedList<>();
        q.add(new int[]{0, 0});
        int re = 1;
        grid[0][0] = -1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                if (cur[0] == grid.length-1 && cur[1] == grid.length-1) {
                    return re;
                }
                for (int[] dir : dirs) {
                    int x = cur[0] + dir[0];
                    int y = cur[1] + dir[1];
                    if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && grid[x][y] == 0) {
                        grid[x][y] = -1;
                        q.add(new int[]{x, y});
                    }
                }
            }
            re++;
        }
        return -1;
    }
}
