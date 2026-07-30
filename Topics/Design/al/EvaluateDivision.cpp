class Solution {
public:
    vector<double> calcEquation(vector<vector<string>>& equations, vector<double>& values, vector<vector<string>>& queries) {
        unordered_map<string, unordered_map<string, double>> graph;
        for (int i = 0; i < equations.size(); ++i) {
            const string& from = equations[i][0];
            const string& to = equations[i][1];

            graph[from][to] = values[i];
            graph[to][from] = 1.0 / values[i];
        }

        vector<double> result;
        result.reserve(queries.size());

        for (const auto& query : queries) {
            unordered_set<string> visited;
            result.push_back(dfs(graph, query[0], query[1], 1.0, visited));
        }
        return result;
    }

private:
    double dfs(const unordered_map<string, unordered_map<string, double>>& graph, const string& current, const string& target, double value, unordered_set<string>& visited) {
        auto it = graph.find(current);
        if (it == graph.end()) {
            return -1.0;
        }

        if (!visited.insert(current).second) { // pair<iterator, bool>
            return -1.0;
        }

        if (current == target) {
            return value;
        }

        for (const auto& [next, rate] : it->second) {
            double answer = dfs(graph, next, target, value * rate, visited);
            if (answer != -1.0) {
                return answer;
            }
        }
        return -1.0;
    }
};

// 第一，如果 query 数量很少，用 DFS/BFS 就足够；如果 query 非常多，我会改成 Weighted Union-Find，把查询降到接近 O(1)。
// 第二，如果要求最大汇率而不是任意汇率，我会把乘积取对数转换成最短路问题，用 Dijkstra。
// 第三，如果需要支持删除关系，Union-Find 就不适用了，需要重新维护图并使用 DFS/BFS。
