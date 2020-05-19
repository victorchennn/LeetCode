package Companies.Google;

import java.util.*;

public class EscapeALargeMaze {

    int[][] dirs = {{0,-1},{0,1},{-1,0},{1,0}};
    int limit = (int)1e6;

    public boolean isEscapePossible(int[][] blocked, int[] source, int[] target) {
        Stack<String> stack = new Stack<>();
        for (int[] b : blocked) {
            stack.add(b[0]+":"+b[1]);
        }
        return bfs(stack, source, target) && bfs(stack, target, source);
    }

    private boolean bfs(Stack<String> blocks, int[] source, int[] target) {
        Set<String> marks = new HashSet<>();
        Queue<int[]> q = new LinkedList<>();
        q.add(source);
        marks.add(source[0]+":"+source[1]);
        while (!q.isEmpty()) {
            int[] cur = q.poll();
            for (int[] dir : dirs) {
                int x = cur[0] + dir[0];
                int y = cur[1] + dir[1];
                if (x < 0 || y < 0 || x >= limit || y >= limit) {
                    continue;
                }
                String key = x + ":" + y;
                if (marks.contains(key) || blocks.contains(key)) {
                    continue;
                }
                if (x == target[0] && y == target[1]) {
                    return true;
                }
                q.add(new int[]{x, y});
                marks.add(key);
            }
            if (marks.size() == 20000) {
                return false;
            }
        }
        return false;
    }
}
