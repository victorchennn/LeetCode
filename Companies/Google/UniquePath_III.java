package Companies.Google;

public class UniquePath_III {
    int sx, sy, ex, ey, empty = 0, re = 0;
    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public int uniquePathsIII(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] >= 0) {
                    empty++;
                }
                if (grid[i][j] == 1) {
                    sx = i;
                    sy = j;
                }
                if (grid[i][j] == 2) {
                    ex = i;
                    ey = j;
                }
            }
        }
        dfs(grid, sx, sy, empty);
        return re;
    }

    private void dfs(int[][] grid, int x, int y, int empty) {
        if (x == ex && y == ey) {
            if (empty == 1) {
                re++;
                return;
            }
        }
        grid[x][y] = -2;
        for (int[] dir : dirs) {
            int xx = x + dir[0];
            int yy = y + dir[1];
            if (xx < 0 || yy < 0 || xx >= grid.length || yy >= grid[0].length || grid[xx][yy] < 0) {
                continue;
            }
            dfs(grid, xx, yy, empty-1);
        }
        grid[x][y] = 0;
    }
}
