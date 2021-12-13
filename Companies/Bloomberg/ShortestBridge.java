package Companies.Bloomberg;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Return the smallest number of 0's you must flip to connect the two islands.
 */
public class ShortestBridge {
    private int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public int shortestBridge(int[][] grid) {
        Queue<int[]> q = new LinkedList<>();
        boolean[][] marks = new boolean[grid.length][grid[0].length];
        boolean found = false;
        for (int i = 0; i < grid.length; i++) {
            if (found) {
                break;
            }
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    dfs(grid, marks, q, i, j);
                    found = true;
                    break;
                }
            }
        }
        int re = 0;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                int[] cur = q.poll();
                for (int[] dir : dirs) {
                    int x = cur[0] + dir[0];
                    int y = cur[1] + dir[1];
                    if (x >= 0 && y >= 0 && x < grid.length && y < grid[0].length && !marks[x][y]) {
                        if (grid[x][y] == 1) {
                            return re;
                        } else {
                            marks[x][y] = true;
                            q.add(new int[]{x, y});
                        }
                    }
                }
            }
            re++;
        }
        return -1;
    }

    private void dfs(int[][] a, boolean[][] marks, Queue<int[]> q, int i, int j) {
        if (i >= 0 && j >= 0 && i < a.length && j < a[0].length && !marks[i][j] && a[i][j] == 1) {
            marks[i][j] = true;
            q.add(new int[]{i, j});
            for (int[] dir : dirs) {
                dfs(a, marks, q, i+dir[0], j+dir[1]);
            }
        }
    }
}
