// You are given a list of airline tickets where tickets[i] = [fromi, toi] represent the departure and the arrival airports of one flight. 
// Reconstruct the itinerary in order and return it.
// Input: tickets = [["JFK","SFO"],["JFK","ATL"],["SFO","ATL"],["ATL","JFK"],["ATL","SFO"]]
// Output: ["JFK","ATL","JFK","SFO","ATL","SFO"]
// Another possible reconstruction is ["JFK","SFO","ATL","JFK","ATL","SFO"] but it is larger in lexical order.
// You may assume all tickets form at least one valid itinerary. You must use all the tickets once and only once.
class Solution {
public:
    vector<string> findItinerary(vector<vector<string>>& tickets，const string& start) {
        unordered_map<string, priority_queue<string, vector<string>, greater<string>>> graph;

        for (auto& t : tickets)
            graph[t[0]].push(t[1]);

        vector<string> route;
        dfs(start, graph, route);
        reverse(route.begin(), route.end());

         // 可选：确认所有票都被使用
        if (route.size() != tickets.size() + 1)
            return {};

        return route;
    }

private:
    void dfs(const string& airport, unordered_map<string, priority_queue<string, vector<string>, greater<string>>>& graph, vector<string>& route) {
        auto& next = graph[airport];

        while (!next.empty()) {
            string destination = next.top();
            next.pop();
            dfs(destination, graph, route);
        }

        route.push_back(airport);
    }
};

// 不需要 visited，因为题目保证 graph 是 DAG，不会形成环。
// path.push_back(next) 后递归，回来再 pop_back()，就是标准 backtracking。

// Given a directed acyclic graph (DAG) of n nodes labeled from 0 to n - 1, 
// find all possible paths from node 0 to node n - 1 and return them in any order.

// Input: graph = [[1,2],[3],[3],[]]
// Output: [[0,1,3],[0,2,3]]
class Solution {
public:
    vector<vector<int>> allPathsSourceTarget(vector<vector<int>>& graph) {
        vector<vector<int>> result;
        vector<int> path{0};

        dfs(graph, 0, path, result);
        return result;
    }

private:
    void dfs(const vector<vector<int>>& graph, int current, vector<int>& path, vector<vector<int>>& result) {
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
