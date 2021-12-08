package Companies.Amazon;

import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class MinimumKnightMoves {

    private Map<String, Integer> memo = new HashMap<>();

    /**
     * DFS
     *
     * Time Complexity: O(∣x⋅y∣)
     * The execution of our recursive algorithm will unfold as a binary tree where each node represents an
     * invocation of the recursive function. And the time complexity of the algorithm is proportional to the
     * total number of invocations, i.e. total number of nodes in the binary tree.
     *
     * The total number of nodes grows exponentially in the binary tree. However, there will be some overlap
     * in terms of the invocations, i.e. dfs(x,y) might be invoked multiple times with the same input.
     * Thanks to the memoization technique, we avoid redundant calculations, i.e. the return value of dfs(x,y)
     * is stored for reuse later, which greatly improves the performance.
     *
     * In the algorithm, we restrict the exploration to the first quadrant of the board. Therefore,
     * in the worst case, we will explore all of the cells between the origin and the target in the first quadrant.
     * In total, there are ∣x⋅y∣ cells in a rectangle that spans from the origin to the target.
     * As a result, the overall time complexity of the algorithm is O(∣x⋅y∣).
     *
     */
    public int minKnightMoves(int x, int y) {
        return dfs(Math.abs(x), Math.abs(y));
    }

    private int dfs(int x, int y) {
        String key = x + "," + y;
        if (memo.containsKey(key)) {
            return memo.get(key);
        }
        if (x+y == 0) {
            return 0;
        } else if (x+y == 2) {
            return 2;
        }
        int re = Math.min(dfs(Math.abs(x-1), Math.abs(y-2)), dfs(Math.abs(x-2), Math.abs(y-1)))+1;
        memo.put(key, re);
        return re;
    }

    /* BFS */
    public int minKnightMovesII(int x, int y) {
        int[][] offsets = {{1, 2}, {2, 1}, {2, -1}, {1, -2},
                {-1, -2}, {-2, -1}, {-2, 1}, {-1, 2}};

        boolean[][] visited = new boolean[607][607];
        Deque<int[]> queue = new LinkedList<>();
        queue.addLast(new int[]{0, 0});
        int steps = 0;
        while (queue.size() > 0) {
            int currLevelSize = queue.size();
            for (int i = 0; i < currLevelSize; i++) {
                int[] curr = queue.removeFirst();
                if (curr[0] == x && curr[1] == y) {
                    return steps;
                }

                for (int[] offset : offsets) {
                    int[] next = new int[]{curr[0] + offset[0], curr[1] + offset[1]};
                    if (!visited[next[0] + 302][next[1] + 302]) {
                        visited[next[0] + 302][next[1] + 302] = true;
                        queue.addLast(next);
                    }
                }
            }
            steps++;
        }
        return steps;
    }
}
