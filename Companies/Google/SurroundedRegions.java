package Companies.Google;

public class SurroundedRegions {

    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public void solve(char[][] board) {
        if (board.length != 0) {
            int m = board.length, n = board[0].length;
            boolean[][] marks = new boolean[m][n];
            for (int i = 0; i < m; i++) {
                if (board[i][0] == 'O') {
                    find(marks, board, i, 0);
                }
                if (board[i][n-1] == 'O') {
                    find(marks, board, i, n-1);
                }
            }
            for (int i = 0; i < n; i++) {
                if (board[0][i] == 'O') {
                    find(marks, board, 0, i);
                }
                if (board[m-1][i] == 'O') {
                    find(marks, board, m-1, i);
                }
            }
            for (int i = 1; i < m-1; i++) {
                for (int j = 1; j < n-1; j++) {
                    if (board[i][j] == 'O' && !marks[i][j]) {
                        board[i][j] = 'X';
                    }
                }
            }
        }
    }

    private void find(boolean[][] marks, char[][] board, int r, int c) {
        for (int[] dir : dirs) {
            int x = r + dir[0];
            int y = c + dir[1];
            if (x < 1 || y < 1 || x > marks.length-2 || y > marks[0].length-2 || board[x][y] == 'X' || marks[x][y]) {
                continue;
            }
            marks[x][y] = true;
            find(marks, board, x, y);
        }
    }
}
