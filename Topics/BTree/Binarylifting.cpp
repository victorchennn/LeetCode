int lastLessOrEqual(const vector<int>& nums, int target) {
    int n = nums.size();
    int pos = -1;

    int jump = 1;
    while (jump < n) {
        jump <<= 1;
    }

    for (; jump > 0; jump >>= 1) {
        int next = pos + jump;

        if (next < n && nums[next] <= target) {
            pos = next;
        }
    }

    return pos;
}

#include <algorithm>
#include <vector>
using namespace std;

class LCA {
private:
    int n;
    int log;
    vector<vector<int>> graph;
    vector<vector<int>> up;
    vector<int> depth;

    void dfs(int node, int parent) {
        up[node][0] = parent;

        for (int j = 1; j < log; ++j) {
            if (up[node][j - 1] == -1) {
                up[node][j] = -1;
            } else {
                up[node][j] =
                    up[up[node][j - 1]][j - 1];
            }
        }

        for (int next : graph[node]) {
            if (next == parent) {
                continue;
            }

            depth[next] = depth[node] + 1;
            dfs(next, node);
        }
    }

public:
    LCA(int n, const vector<pair<int, int>>& edges, int root = 0)
        : n(n),
          log(1),
          graph(n),
          depth(n, 0) {

        while ((1 << log) <= n) {
            ++log;
        }

        up.assign(n, vector<int>(log, -1));

        for (auto [u, v] : edges) {
            graph[u].push_back(v);
            graph[v].push_back(u);
        }

        dfs(root, -1);
    }

    int kthAncestor(int node, int k) {
        for (int j = 0; j < log; ++j) {
            if (k & (1 << j)) {
                node = up[node][j];

                if (node == -1) {
                    return -1;
                }
            }
        }

        return node;
    }

    int lowestCommonAncestor(int u, int v) {
        if (depth[u] < depth[v]) {
            swap(u, v);
        }

        // 先让 u 和 v 到同一深度
        int diff = depth[u] - depth[v];
        u = kthAncestor(u, diff);

        if (u == v) {
            return u;
        }

        // 从最大的跳跃距离开始
        for (int j = log - 1; j >= 0; --j) {
            if (up[u][j] != up[v][j]) {
                u = up[u][j];
                v = up[v][j];
            }
        }

        return up[u][0];
    }
};
