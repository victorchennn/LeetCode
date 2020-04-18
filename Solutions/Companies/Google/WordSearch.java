package Companies.Google;

public class WordSearch {

    int[][] dirs = {{0,1},{0,-1},{-1,0},{1,0}};

    public boolean exist(char[][] board, String word) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (dfs(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean dfs(char[][] board, String word, int i, int j, int s) {
        if (s == word.length()) {
            return true;
        }
        if (i >= 0 && j >= 0 && i < board.length && j < board[0].length && board[i][j] == word.charAt(s)) {
            board[i][j] = '$';
            for (int[] dir : dirs) {
                if (dfs(board, word, i+dir[0], j+dir[1], s+1)) {
                    return true;
                }
            }
            board[i][j] = word.charAt(s);
        }
        return false;
    }
}
