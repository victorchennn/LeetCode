package Companies.Microsoft;

import java.util.HashSet;
import java.util.Set;

public class SudokuValid {
    public boolean isValidSudoku(char[][] board) {
        for (int i = 0; i < 9; i++) {
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
        for (int i = 0; i < 9; i++) {
            Set<Character> set = new HashSet<>();
            for (int r = 0; r < 3; r++) {
                for (int c = 0; c < 3; c++) {
                    int row = i/3*3+r;
                    int col = i%3*3+c;
                    if (board[row][col] != '.' && !set.add(board[row][col])) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}
