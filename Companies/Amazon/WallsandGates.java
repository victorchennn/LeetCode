package Companies.Amazon;

public class WallsandGates {
    public void wallsAndGates(int[][] rooms) {
        for (int i = 0; i < rooms.length; i++) {
            for (int j = 0; j < rooms[0].length; j++) {
                if (rooms[i][j] == 0) {
                    dfs(rooms, i, j, 0);
                }
            }
        }
    }

    private void dfs(int[][] rooms, int i, int j, int step) {
        if (i < 0 || j < 0 || i >= rooms.length || j >= rooms[0].length || rooms[i][j] < step) {
            return;
        }
        rooms[i][j] = step;
        dfs(rooms, i+1, j, step+1);
        dfs(rooms, i, j+1, step+1);
        dfs(rooms, i-1, j, step+1);
        dfs(rooms, i, j-1, step+1);
    }
}
