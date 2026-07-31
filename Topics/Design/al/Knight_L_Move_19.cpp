// from 1 to 9
// 1 2 3
// 4 5 6
// 7 8 9

class Solution {
public: // 它不是因为 DFS 天生不用 visited，而是因为深度被maxMoves限制住了。
    vector<vector<int>> knightPaths(int maxMoves = 10) { 
        vector<vector<int>> graph = {
            {}, {6, 8}, {7, 9}, {4, 8}, {3, 9}, {},  {1, 7}, {2, 6}, {1, 3}, {2, 4}
        };

        vector<vector<int>> result;
        vector<int> path{1};

        dfs(graph, 1, 0, maxMoves, path, result);

        return result;
    }

private:
    void dfs(const vector<vector<int>>& graph,
             int cur, int moves, int maxMoves,
             vector<int>& path,
             vector<vector<int>>& result) {

        if (cur == 9) {
            result.push_back(path);
            return;
        }

        if (moves == maxMoves)
            return;

        for (int next : graph[cur]) {
            path.push_back(next);
            dfs(graph, next, moves + 1, maxMoves, path, result);
            path.pop_back();
        }
    }
};

// 最短路径？ BFS
class Solution {
public:
    vector<int> shortestKnightPath() {
        vector<vector<int>> graph = {
            {}, {6, 8}, {7, 9}, {4, 8}, {3, 9}, {}, {1, 7}, {2, 6}, {1, 3}, {2, 4}
        };

        queue<int> q;
        vector<bool> visited(10, false);
        vector<int> parent(10, -1);

        q.push(1);
        visited[1] = true;

        while (!q.empty()) {
            int cur = q.front();
            q.pop();

            if (cur == 9) break;

            for (int next : graph[cur]) {
                if (visited[next]) continue;

                visited[next] = true;
                parent[next] = cur;
                q.push(next);
            }
        }

        if (!visited[9]) return {};

        vector<int> path;
        for (int cur = 9; cur != -1; cur = parent[cur])
            path.push_back(cur);

        reverse(path.begin(), path.end());
        return path;
    }
};
