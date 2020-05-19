package Companies.Google;

public class IslandPerimeter {
    /**
     * Each square has 4 sides, 'neighbor' counts right and down side square, if exists,
     * will reduce two sides, one is mine and the other one is his, like my right side and
     * his left side, or my down side and his up side.
     */
    public int islandPerimeter(int[][] grid) {
        int num = 0, neigh = 0;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (grid[i][j] == 1) {
                    num++;
                    if (i < grid.length-1 && grid[i+1][j] == 1) {
                        neigh++;
                    }
                    if (j < grid[0].length-1 && grid[i][j+1] == 1) {
                        neigh++;
                    }
                }
            }
        }
        return num*4-neigh*2;
    }
}
