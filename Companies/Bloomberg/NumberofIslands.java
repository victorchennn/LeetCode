package Companies.Bloomberg;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @Follow-up1: sum of perimeter of islands?
 * @see IslandPerimeter
 *
 * @Follow-up2: maximal area island?
 * @see MaxAreaofIsland
 */
public class NumberofIslands {
    private static int[][] dirs = {{0,-1},{-1,0},{0,1},{1,0}};

    public static int numIslands(char[][] grid) {
        int re = 0;
        int m = grid.length, n = grid[0].length;
//        int num = 0, neigh = 0;
        boolean[][] marks = new boolean[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1' && !marks[i][j]) {
                    re++;
                    dfs(grid, i, j, marks);
//                    num += temp[0];
//                    neigh += temp[1];
                }
            }
        }
//        int sum = num*4 - neigh*2; // sum of perimeter of islands
        return re;
    }

    private static void dfs(char[][] grid, int x, int y, boolean[][] marks) {
        marks[x][y] = true;
//        int num = 1, neigh = 0;
//        if (x < grid.length-1 && grid[x+1][y] == '1') {
//            neigh++;
//        }
//        if (y < grid[0].length-1 && grid[x][y+1] == '1') {
//            neigh++;
//        }
        for (int[] dir : dirs) {
            int xx = x + dir[0];
            int yy = y + dir[1];
            if (xx < 0 || yy < 0 || xx >= grid.length || yy >= grid[0].length || grid[xx][yy] == '0' || marks[xx][yy]) {
                continue;
            }
            dfs(grid, xx, yy, marks);
//            num += temp[0];
//            neigh += temp[1];
        }
//        return new int[]{num, neigh};
    }

    public int numIslandsII(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }
        int m = grid.length, n = grid[0].length, num = 0;

        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == '1') {
                    num++;
                    grid[i][j] = '0'; // mark as visited
                    Queue<Integer> neighbors = new LinkedList<>();
                    neighbors.add(i*n + j);
                    while (!neighbors.isEmpty()) {
                        int id = neighbors.remove();
                        for (int[] dir : dirs) {
                            int row = id / n + dir[0];
                            int col = id % n + dir[1];
                            if (row >= 0 && row < m && col >= 0 && col < n && grid[row][col] == '1') {
                                neighbors.add(row*n + col);
                                grid[row][col] = '0';
                            }
                        }

                    }
                }
            }
        }
        return num;
    }

    public static void main(String...args) {
        System.out.println(numIslands(new char[][]{
                {'1','1','1','1','0'},
                {'1','1','0','1','0'},
                {'1','1','0','0','0'},
                {'0','0','0','0','0'}}));
        System.out.println(numIslands(new char[][]{
                {'1','1','0','0','0'},
                {'1','1','0','0','0'},
                {'0','0','1','0','0'},
                {'0','0','0','1','1'}}));

    }
}