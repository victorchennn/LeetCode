package Companies.Microsoft;

public class MaximalAreaofIsland {
    private int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public int maxAreaOfIsland(int[][] grid) {
        int max = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    max = Math.max(max, dfs(grid, i, j));
                }
            }
        }
        return max;
    }

    private int dfs(int[][] grid, int i, int j) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == 0) {
            return 0;
        }
        int re = 1;
        grid[i][j] = 0;
        for (int[] dir : dirs) {
            re += dfs(grid, i+dir[0], j+dir[1]);
        }
        return re;
    }
}
