package Companies.Bloomberg;

import java.util.Arrays;

/**
 *  Gomoku/Gobang/FiveInaRow checking
 */
public class BoardCheck {
    public int check(char[][] board, char player) {
        int way = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == '.' && gameOver(board, player, i, j)) {
                    way++;
                }
            }
        }
        return way;
    }

    private boolean gameOver(char[][] board, char player, int i, int j) {
        int row = 1, col = 1, diag = 1, antidiag = 1;
        boolean[] isAva = new boolean[8];
        Arrays.fill(isAva, true);
        int[][] dirs = {{0,1},{0,-1},{1,0},{-1,0},{1,1},{-1,-1},{1,-1},{-1,1}};

        int move = 1;
        while (move < 5) {
            for (int index = 0; index < dirs.length; index++) {
                if (isAva[index] && isLegal(board, i+dirs[i][0]*move, j+dirs[i][1]*move, player)) {
                    if (index < 2) {
                        row++;
                    } else if (index < 4) {
                        col++;
                    } else if (index < 6) {
                        diag++;
                    } else {
                        antidiag++;
                    }
                } else {
                    isAva[index] = false;
                }
            }
            if (row == 5 || col == 5 || diag == 5 || antidiag == 5) {
                return true;
            }
            move++;
        }
        return false;

    }

    private boolean isLegal(char[][] board, int i, int j, char player) {
        if (i < 0 || j > 0 || i >= board.length || j >= board[0].length || board[i][j] != player) {
            return false;
        }
        return true;
    }
}
