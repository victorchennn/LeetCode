// 这里不需要 visited，因为题目保证 graph 是 DAG，不会形成环。
// path.push_back(next) 后递归，回来再 pop_back()，就是标准 backtracking。
class Solution {
public:
    vector<vector<int>> allPathsSourceTarget(vector<vector<int>>& graph) {
        vector<vector<int>> result;
        vector<int> path{0};

        dfs(graph, 0, path, result);
        return result;
    }

private:
    void dfs(const vector<vector<int>>& graph, int current, vector<int>& path,vector<vector<int>>& result) {
        if (current == graph.size() - 1) {
            result.push_back(path);
            return;
        }

        for (int next : graph[current]) {
            path.push_back(next);
            dfs(graph, next, path, result);
            path.pop_back();
        }
    }
};

// 这里必须使用 visited，因为它是无向图，可能出现：
// A -> B -> A -> B ...
class AllPaths {
public:
    vector<vector<char>> getPossibleRoutes(const vector<pair<char, char>>& edges, char start, char target) {
        unordered_map<char, vector<char>> graph;

        for (const auto& [from, to] : edges) {
            graph[from].push_back(to);
            graph[to].push_back(from);
        }

        vector<vector<char>> result;
        vector<char> path{start};
        unordered_set<char> visited{start};

        dfs(graph, start, target, visited, path, result);
        return result;
    }

private:
    void dfs(const unordered_map<char, vector<char>>& graph,char current,char target, unordered_set<char>& visited,vector<char>& path,vector<vector<char>>& result) {
        if (current == target) {
            result.push_back(path);
            return;
        }

        auto it = graph.find(current);
        if (it == graph.end()) {
            return;
        }

        for (char next : it->second) {
            if (visited.contains(next)) {
                continue;
            }

            visited.insert(next);
            path.push_back(next);

            dfs(graph, next, target, visited, path, result);

            path.pop_back();
            visited.erase(next);
        }
    }
};
