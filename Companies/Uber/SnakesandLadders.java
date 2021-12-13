package Companies.Uber;

import java.util.LinkedList;
import java.util.Queue;

public class SnakesandLadders {
    public int snakesAndLadders(int[][] board) {
        int len = board.length;
        boolean[] visited = new boolean[len*len+1];
        visited[1] = true;
        Queue<Integer> q = new LinkedList<>();
        q.add(1);
        int steps = 1;
        while (!q.isEmpty()) {
            int size = q.size();
            for (int s = 0; s < size; s++) {
                int cur = q.poll();
                for (int i = 1; i <= 6; i++) {
                    int next = cur + i;
                    int[] pos = getPosition(next, len);
                    if (board[pos[0]][pos[1]] > 0) {
                        next = board[pos[0]][pos[1]];
                    }
                    if (next == len * len) {
                        return steps;
                    }
                    if (!visited[next]) {
                        q.add(next);
                        visited[next] = true;
                    }
                }
            }
            steps++;
        }
        return -1;
    }

    private int[] getPosition(int index, int len) {
        int row = (index-1)/len, col = (index-1)%len;
        int x = len-1-row;
        int y = row%2 == 0? col:len-1-col;
        return new int[]{x, y};
    }


}
