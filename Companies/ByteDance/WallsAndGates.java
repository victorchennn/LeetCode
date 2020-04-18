package Companies.ByteDance;

public class WallsAndGates {

    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public void wallsAndGates(int[][] rooms) {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) {
                    dfs(rooms, i, j, 0);
                }
            }
        }
    }

    private void dfs(int[][] rooms, int i, int j, int d) {
        if (i < 0 || j < 0 || i >= rooms.length || j >= rooms[0].length || rooms[i][j] < d) { // smaller than d, not equal
            return;
        }
        rooms[i][j] = d;
        for (int[] dir : dirs) {
            dfs(rooms, i + dir[0], j + dir[1], d+1);
        }
    }
}
