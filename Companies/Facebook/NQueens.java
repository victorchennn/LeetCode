package Companies.Facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NQueens {
    public List<List<String>> solveNQueens(int n) {
        char[][] board = new char[n][n];
        for (int i = 0; i < n; i++) {
            Arrays.fill(board[i], '.');
        }
        List<List<String>> re = new ArrayList<>();
        dfs(board, 0, re);
        return re;
    }

    private void dfs(char[][] board, int colIndex, List<List<String>> re) {
        if (colIndex == board.length) {
            List<String> l = new ArrayList<>();
            for (int i = 0; i < board.length; i++) {
                l.add(new String(board[i]));
            }
            re.add(l);
            return;
        }
        for (int i = 0; i < board.length; i++) {
            if (validate(board, i, colIndex)) {
                board[i][colIndex] = 'Q';
                dfs(board, colIndex+1, re);
                board[i][colIndex] = '.';
            }
        }
    }

    private boolean validate(char[][] board, int i, int j) {
        for (int r = 0; r < board.length; r++) {
            for (int c = 0; c < j; c++) {
                if (board[r][c] == 'Q' && (r == i || r+c == i+j || r+j == c+i)) {
                    return false;
                }
            }
        }
        return true;
    }
}
