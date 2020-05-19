package Companies.Bloomberg;

public class GameofLife {
    public void gameOfLife(int[][] board) {
        int[][] dirs = new int[][]{{1,1},{1,0},{1,-1},{0,-1},{0,1},{-1,1},{-1,0},{-1,-1}};
        int row = board.length;
        int col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                int neigh = 0;
                for (int[] dir : dirs) {
                    int rr = i + dir[0];
                    int cc = j + dir[1];
                    if (rr >= 0 && cc >= 0 && rr < row && cc < col && Math.abs(board[rr][cc]) == 1) { // absolute value
                        neigh++;
                    }
                }
                if (board[i][j] == 1 && (neigh < 2 || neigh > 3)) {
                    board[i][j] = -1;
                }
                if (board[i][j] == 0 && neigh == 3) {
                    board[i][j] = 2;
                }
            }
        }
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (board[i][j] > 0) {
                    board[i][j] = 1;
                } else {
                    board[i][j] = 0;
                }
            }
        }
    }
}
