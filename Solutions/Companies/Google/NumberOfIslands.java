package Companies.Google;

public class NumberOfIslands {

    boolean[][] marks;
    int[][] dirs = {{0,-1},{-1,0},{0,1},{1,0}};

    public int numIslands(char[][] grid) {
        int re = 0;
        int m = grid.length, n = grid[0].length;
        marks = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !marks[i][j]) {
                    re++;
                    dfs(grid, i, j, marks);
                }
            }
        }
        return re;
    }

    private void dfs(char[][] grid, int x, int y, boolean[][] marks) {
        marks[x][y] = true;
        for (int[] dir : dirs) {
            int xx = x + dir[0];
            int yy = y + dir[1];
            if (xx < 0 || yy < 0 || xx >= grid.length || yy >= grid[0].length || grid[xx][yy] == '0' || marks[xx][yy]) {
                continue;
            }
            dfs(grid, xx, yy, marks);
        }
    }
}
