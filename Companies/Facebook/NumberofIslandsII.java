package Companies.Facebook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumberofIslandsII {
    private int[][] dirs = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};

    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        List<Integer> re = new ArrayList<>();
        int[] board = new int[m*n];
        Arrays.fill(board, -1);
        int count = 0;
        for (int[] pos : positions) {
            int x = pos[0];
            int y = pos[1];
            int index = x*n+y;
            if (board[index] != -1) { // duplicate
                re.add(count);
                continue;
            }
            count++;
            board[index] = index;
            for (int[] dir : dirs) {
                int xx = x + dir[0];
                int yy = y + dir[1];
                int iindex = xx*n + yy;
                if (xx < 0 || yy < 0 || xx >= m || yy >= n || board[iindex] == -1) {
                    continue;
                }
                int parent = find(board, iindex);
                if (index != parent) {
                    board[index] = parent;
                    index = parent;
                    count--;
                }
            }
            re.add(count);
        }
        return re;
    }

    private int find(int[] board, int index) {
        if (board[index] != index) {
            board[index] = find(board, board[index]);
        }
        return board[index];
    }
}
