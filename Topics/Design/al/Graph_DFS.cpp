// 给了一些 (node1, node2) 的数对 ，表示这两个nodes是相连的 - 让输出一个 adjacency list，写出每个 node 能抵达哪些其他的 nodes
// input:
// [('A','B'),('C','D'),('E','F'),('A','C'),('F',None),('D',None)]

// output:
// A -> (B, C, D)
// E -> F

class Solution {
public:
    unordered_map<char, vector<char>> reachableNodes(const vector<pair<char, optional<char>>>& edges) {
        unordered_map<char, vector<char>> graph;
        unordered_set<char> allNodes;

        for (const auto& [from, to] : edges) {
            allNodes.insert(from);
            graph[from];  // 保证没有 outgoing edge 的节点也存在

            if (to.has_value()) {
                graph[from].push_back(*to);
                allNodes.insert(*to);
                graph[*to];
            }
        }

        unordered_map<char, vector<char>> result;

        for (char start : allNodes) {
            unordered_set<char> visited;
            dfs(start, graph, visited);

            visited.erase(start);  // 不把自己算作可到达节点

            if (!visited.empty()) {
                result[start] = vector<char>(
                    visited.begin(), visited.end()
                );

                sort(result[start].begin(), result[start].end());
            }
        }

        return result;
    }

private:
    void dfs(char node, const unordered_map<char, vector<char>>& graph, unordered_set<char>& visited) {
        if (!visited.insert(node).second) { // pair<iterator, bool> 防止图里有环
            return;
        }

        auto it = graph.find(node);
        if (it == graph.end()) { // 当前节点没有 outgoing edge。
            return;
        }

        for (char next : it->second) {
            dfs(next, graph, visited);
        }
    }
};
