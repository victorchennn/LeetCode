package Companies.Amazon;

import java.util.stream.IntStream;

public class FriendCircles {
    public int findCircleNum(int[][] M) {
        /* Union-Find */
        int len = M.length, count = M.length;
        int[] parents = IntStream.range(0, len).toArray();
        for (int i = 0; i < len; i++) {
            for (int j = i+1; j < len; j++) {
                if (M[i][j] == 1) {
                    int x = find(parents, i);
                    int y = find(parents, j);
                    if (x != y) {
                        parents[x] = y;
                        count--;
                    }
                }
            }
        }
        return count;

        /* DFS */
//        int[] visited = new int[M.length];
//        int count = 0;
//        for (int i = 0; i < M.length; i++) {
//            if (visited[i] == 0) {
//                dfs(M, visited, i);
//                count++;
//            }
//        }
//        return count;

        /* BFS */
//        Queue<Integer> q = new LinkedList<>();
//        for (int i = 0; i < M.length; i++) {
//            if (visited[i] == 0) {
//                q.add(i);
//                count++;
//                while (!q.isEmpty()) {
//                    int cur = q.poll();
//                    visited[cur] = 1;
//                    for (int j = 0; j < M.length; j++) {
//                        if (M[cur][j] == 1 && visited[j] == 0) {
//                            q.add(j);
//                        }
//                    }
//                }
//            }
//        }
//        return count;
    }

    private void dfs(int[][] M, int[] visited, int i) {
        for (int j = 0; j < M.length; j++) {
            if (M[i][j] == 1 && visited[j] == 0) {
                visited[j] = 1;
                dfs(M, visited, j);
            }
        }
    }

    private int find(int[] parents, int x) {
        if (parents[x] != x) {
            parents[x] = find(parents, parents[x]);
        }
        return parents[x];
    }
}
