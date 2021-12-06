package Companies.Amazon;

/**
 * Given a m * n matrix of ones and zeros, return how many square submatrices have all ones.
 *
 *
 * dp[i][j] means the size of biggest square with A[i][j] as bottom-right corner.
 * dp[i][j] also means the number of squares with A[i][j] as bottom-right corner.
 *
 * If A[i][j] == 0, no possible square.
 * If A[i][j] == 1,
 * we compare the size of square dp[i-1][j-1], dp[i-1][j] and dp[i][j-1].
 * min(dp[i-1][j-1], dp[i-1][j], dp[i][j-1]) + 1 is the maximum size of square that we can find.
 */
public class CountSquareSubmatriceswithAllOnes {
    public int countSquares(int[][] matrix) {
        int re = 0;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (i > 0 && j > 0 && matrix[i][j] > 0) {
                    matrix[i][j] = Math.min(matrix[i-1][j-1], Math.min(matrix[i-1][j], matrix[i][j-1]))+1;
                }
                re += matrix[i][j];
            }
        }
        return re;
    }
}
