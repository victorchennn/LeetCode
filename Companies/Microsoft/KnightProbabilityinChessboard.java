package Companies.Microsoft;

public class KnightProbabilityinChessboard {
    public double knightProbability(int N, int K, int r, int c) {
        double[][] board = new double[N][N];
        board[r][c] = 1;
        int[][] dirs = {{1,2},{2,1},{-1,-2},{-2,-1},{1,-2},{2,-1},{-2,1},{-1,2}};
        for (; K > 0; K--) {
            double[][] temp = new double[N][N];
            for (int i = 0; i < N; i++) {
                for (int j = 0; j < N; j++) {
                    for (int[] dir : dirs) {
                        int rr = i + dir[0];
                        int cc = j + dir[1];
                        if (rr >= 0 && cc >= 0 && rr < N && cc < N) {
                            temp[rr][cc] += board[i][j]/8.0;
                        }
                    }
                }
            }
            board = temp;
        }
        double re = 0;
        for (double[] row : board) {
            for (double p : row) {
                re += p;
            }
        }
        return re;
    }
}
