package Companies.Google;

import java.util.LinkedList;
import java.util.Queue;

public class ShortestBridge {
    private int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public int shortestBridge(int[][] A) {
        Queue<int[]> q = new LinkedList<>();
        boolean[][] marks = new boolean[A.length][A[0].length];
        boolean found = false;
        for (int i = 0; i < A.length; i++) {
            if (found) {
                break;
            }
            for (int j = 0; j < A[0].length; j++) {
                if (A[i][j] == 1) {
                    dfs(A, marks, q, i, j);
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
                    if (x >= 0 && y >= 0 && x < A.length && y < A[0].length && !marks[x][y]) {
                        if (A[x][y] == 1) {
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
