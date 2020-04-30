package Companies.Amazon;

import java.util.PriorityQueue;

public class TrappingRainWaterII {
    private int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public int trapRainWater(int[][] heightMap) {
        if (heightMap == null || heightMap.length < 2 || heightMap[0].length < 2) {
            return 0;
        }
        PriorityQueue<Cell> q = new PriorityQueue<>((a, b)->a.height-b.height); // can be replaced by compareTo in the class
        int m = heightMap.length, n = heightMap[0].length;
        boolean[][] visited = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            visited[i][0] = true;
            visited[i][n-1] = true;
            q.add(new Cell(i, 0, heightMap[i][0]));
            q.add(new Cell(i, n-1, heightMap[i][n-1]));
        }
        for (int i = 1; i < n-1; i++) {
            visited[0][i] = true;
            visited[m-1][i] = true;
            q.add(new Cell(0, i, heightMap[0][i]));
            q.add(new Cell(m-1, i, heightMap[m-1][i]));
        }
        int re = 0;
        while (!q.isEmpty()) {
            Cell cur = q.poll();
            for (int[] dir : dirs) {
                int r = cur.row + dir[0];
                int c = cur.col + dir[1];
                if (r >= 0 && r < m && c >= 0 && c < n && !visited[r][c]) {
                    visited[r][c] = true;
                    re += Math.max(0, cur.height-heightMap[r][c]);
                    q.add(new Cell(r, c, Math.max(cur.height, heightMap[r][c]))); // !!!
                }
            }
        }
        return re;
    }

    class Cell implements Comparable<Cell>{ //!!!!!
        int row, col, height;
        Cell(int _row, int _col, int _height) {
            row = _row;
            col = _col;
            height = _height;
        }

        public int compareTo(Cell c) {
            return this.height-c.height;
        }
    }
}
