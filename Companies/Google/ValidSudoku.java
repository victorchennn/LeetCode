package Companies.Google;

import java.util.HashSet;
import java.util.Set;

public class ValidSudoku {
    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < 9; i ++) {
            Set<Character> rows = new HashSet<>();
            Set<Character> cols = new HashSet<>();
            for (int j = 0; j < 9; j++) {
                if (board[i][j] != '.' && !rows.add(board[i][j])) {
                    return false;
                }
                if (board[j][i] != '.' && !cols.add(board[j][i])) {
                    return false;
                }
            }
        }
        for (int index = 0; index < 9; index++) {
            Set<Character> seen = new HashSet<>();
            for (int row = 0; row < 3; row++) {
                for (int col = 0; col < 3; col++) {
                    int rr = index/3*3 + row;
                    int cc = index%3*3 + col;
                    if (board[rr][cc] != '.' && !seen.add(board[rr][cc])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
