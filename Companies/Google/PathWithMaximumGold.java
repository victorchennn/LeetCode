package Companies.Google;

public class PathWithMaximumGold {
    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public int getMaximumGold(int[][] grid) {
        int re = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] != 0) {
                    int gold = dfs(grid, i, j);
                    re = Math.max(re, gold);
                }
            }
        }
        return re;
    }

    private int dfs(int[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] <= 0) {
            return 0;
        }
        int re = grid[i][j];
        int gold = grid[i][j];
        grid[i][j] = -1;
        for (int[] dir : dirs) {
            re = Math.max(re, gold + dfs(grid, i+dir[0], j+dir[1]));
        }
        grid[i][j] = gold;
        return re;
    }
}
