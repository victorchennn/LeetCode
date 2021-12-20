package Companies.Microsoft;

import java.util.HashSet;
import java.util.Set;

public class NumberofDistinctIslands {
    private int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0}};

    public int numDistinctIslands(int[][] grid) {
        Set<String> s = new HashSet<>();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    StringBuilder sb = new StringBuilder();
                    dfs(sb, i, j, 0, 0, grid);
                    s.add(sb.toString());
                }
            }
        }
        return s.size();
    }

    private void dfs(StringBuilder sb, int i, int j, int x, int y, int[][] grid) {
        if (i < 0 || j < 0 || i >= grid.length || j >= grid[0].length || grid[i][j] == 0) {
            return;
        }
        grid[i][j] = 0;
        sb.append(x + "" + y);
        for (int[] dir : dirs) {
            dfs(sb, i+dir[0], j+dir[1], x+dir[0], y+dir[1], grid);
        }
    }
}
