#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>

using namespace std;

class CloneGraph {
public:
    // BFS
    Node* cloneGraph(Node* node) {
        if (!node) {
            return NULL;
        }
        Node* copy = new Node(node->val, {});
        unordered_map<Node*, Node*> visited{};
        queue<Node*> q{};
        visited[node] = copy;
        q.push(node);
        while (!q.empty()) {
            Node* cur = q.front();
            q.pop();
            for (Node* neigh : cur->neighbors) {
                if (visited.find(neigh) == visited.end()) {
                    visited[neigh] = new Node(neigh->val, {});
                    q.push(neigh);
                }
                visited[cur]->neighbors.push_back(visited[neigh]);
            }
        }
        return copy;
    }

    // DFS
    Node* cloneGraphII(Node* node) {
        if (!node) {
            return NULL;
        }
        if (dfs.find(node) == dfs.end()) {
            dfs[node] = new Node(node->val, {});
            for (Node* neigh : node->neighbors) {
                dfs[node]->neighbors.push_back(cloneGraphII(neigh));
            }
        }
        return dfs[node];
    }

private:
    unordered_map<Node*, Node*> dfs;
};

class Node {
public:
    int val;
    vector<Node*> neighbors;

    Node() {
        val = 0;
        neighbors = vector<Node*>();
    }

    Node(int _val) {
        val = _val;
        neighbors = vector<Node*>();
    }

    Node(int _val, vector<Node*> _neighbors) {
        val = _val;
        neighbors = _neighbors;
    }
};