package Companies.Amazon;

public class Minesweeper {
    public char[][] updateBoard(char[][] board, int[] click) {
        int m = board.length, n = board[0].length;
        int[][] dirs = {{-1,-1},{-1,0},{-1,1},{0,-1},{0,1},{1,-1},{1,0},{1,1}};
        int x = click[0], y = click[1];
        if (board[x][y] == 'M') {
            board[x][y] = 'X';
        } else {
            int count = 0;
            for (int[] dir : dirs) {
                int rr = x + dir[0];
                int cc = y + dir[1];
                if (rr < 0 || cc < 0 || rr >= m || cc >= n) {
                    continue;
                }
                if (board[rr][cc] == 'M') {
                    count++;
                }
            }
            if (count > 0) {
                board[x][y] = (char)(count+'0');
            } else {
                board[x][y] = 'B';
                for (int[] dir : dirs) {
                    int rr = x + dir[0];
                    int cc = y + dir[1];
                    if (rr < 0 || cc < 0 || rr >= m || cc >= n) {
                        continue;
                    }
                    if (board[rr][cc] == 'E') {
                        updateBoard(board, new int[]{rr,cc});
                    }
                }
            }
        }
        return board;
    }
}
