package Companies.Google;

/**
 * One Dimension check:
 * @see Companies.Bloomberg.CandyCrush
 */
public class CandyCrush {
    public int[][] candyCrush(int[][] board) {
        int m = board.length, n = board[0].length;
        boolean found = true;
        while (found) {
            found = false;
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    int val = Math.abs(board[i][j]);
                    if (val == 0) {
                        continue;
                    }
                    if (j < n-2 && Math.abs(board[i][j+1]) == val && Math.abs(board[i][j+2]) == val) {
                        found = true;
                        int index = j;
                        while (index < n && Math.abs(board[i][index]) == val) {
                            board[i][index++] = -val;
                        }
                    }
                    if (i < m-2 && Math.abs(board[i+1][j]) == val && Math.abs(board[i+2][j]) == val) {
                        found = true;
                        int index = i;
                        while (index < m && Math.abs(board[index][j]) == val) {
                            board[index++][j] = -val;
                        }
                    }
                }
            }
            if (found) {
                for (int j = 0; j < n; j++) {
                    int p = m-1;
                    for (int i = m-1; i >= 0; i--) {
                        if (board[i][j] > 0) {
                            board[p--][j] = board[i][j];
                        }
                    }
                    for (int k = p; k >= 0; k--) {
                        board[k][j] = 0;
                    }
                }
            }
        }
        return board;
    }
}
