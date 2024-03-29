package Companies.GoldmanSachs;

public class NumberofProvinces {
    public int findCircleNum(int[][] M) {
        // DFS
        int[] visited = new int[M.length];
        int count = 0;
        for (int i = 0; i < M.length; i++) {
            if (visited[i] == 0) {
                dfs(M, visited, i);
                count++;
            }
        }

        // BFS
//        Queue<Integer> q = new LinkedList<>();
//        for (int i = 0; i < M.length; i++) {
//            if (visited[i] == 0) {
//                q.add(i);
//                while (!q.isEmpty()) {
//                    int s = q.poll();
//                    visited[s] = 1;
//                    for (int j = 0; j < M.length; j++) {
//                        if (M[s][j] == 1 && visited[j] == 0) {
//                            q.add(j);
//                        }
//                    }
//                }
//                count++;
//            }
//        }

        return count;
    }

    private void dfs(int[][] M, int[] visited, int i) {
        for (int j = 0; j < M.length; j++) {
            if (M[i][j] == 1 && visited[j] == 0) {
                visited[j] = 1;
                dfs(M, visited, j);
            }
        }
    }


}
