package Topics.Design;

public class TicTacToe {

    private int[] rows, cols;
    private int diag, antidiag;

    /** Initialize your data structure here. */
    public TicTacToe(int n) {
        rows = new int[n];
        cols = new int[n];
    }

    /** Player {player} makes move at ({row}, {col}).
     @param row The row of the board.
     @param col The column of the board.
     @param player The player, can be either 1 or 2.
     @return The current winning condition, can be either:
     0: No one wins.
     1: Player 1 wins.
     2: Player 2 wins. */
    public int move(int row, int col, int player) {
        int n = rows.length;
        int add = player == 1?1:-1;
        rows[row] += add;
        cols[col] += add;
        if (row == col) {
            diag += add;
        }
        if (row == n-col-1) {
            antidiag += add;
        }
        if (Math.abs(rows[row]) == n || Math.abs(cols[col]) == n ||
                Math.abs(diag) == n || Math.abs(antidiag) == n) {
            return player;
        }
        return 0;
    }
}
