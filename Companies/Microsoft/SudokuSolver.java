package Companies.Microsoft;

public class SudokuSolver {
    public void solveSudoku(char[][] board) {
        dfs(board, 0, 0);
    }

    private boolean dfs(char[][] board, int x, int y) {
        if (x == 9) {
            return true;
        }
        if (y == 9) {
            return dfs(board, x+1, 0);
        }
        if (board[x][y] != '.') {
            return dfs(board, x, y+1);
        }
        for (char c = '1'; c <= '9'; c++) {
            if (isValid(board, x, y, c)) {
                board[x][y] = c;
                if (dfs(board, x, y+1)) {
                    return true;
                }
                board[x][y] = '.';
            }
        }
        return false;
    }

    private boolean isValid(char[][] board, int x, int y, char c) {
        for (int i = 0; i < 9; i++) {
            if (board[x][i] == c || board[i][y] == c || board[x/3*3+i/3][y/3*3+i%3] == c) {
                return false;
            }
        }
        return true;
    }
}
