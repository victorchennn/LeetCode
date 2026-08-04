// Input array: [-1, 0, 0, 1, 1, 4] ,  given index: 1
// Output: [1, 3, 4, 5]
class Solution {
public:
    vector<int> getSubtree(const vector<int>& parent, int node) {
        int n = parent.size();
        if (node < 0 || node >= n) {
            return {};
        }

        vector<vector<int>> children(n);
      
        for (int child = 0; child < n; ++child) {
            int p = parent[child];
            if (p != -1) {
                if (p < 0 || p >= n) {
                    return {};  // 非法 parent index
                }

                children[p].push_back(child);
            }
        }

        vector<int> result;
        dfs(node, children, result);
        return result;
    }

private:
    void dfs(int node, const vector<vector<int>>& children, vector<int>& result) {
        result.push_back(node);

        for (int child : children[node]) {
            dfs(child, children, result);
        }
    }
};

// O(1) 这个解法会破坏原始 parent 数组。如果输入必须保持不变，就不能这样做。
class Solution {
public:
    vector<int> getSubtree(vector<int>& parent, int target) {
        int n = parent.size();

        if (target < 0 || target >= n) {
            return {};
        }

        // 把原来的 root 从 -1 改成指向自己
        for (int i = 0; i < n; ++i) {
            if (parent[i] == -1) {
                parent[i] = i;
            }
        }

        // 将 target 从原树中断开，成为一个新的 root
        parent[target] = target;

        vector<int> result;

        for (int node = 0; node < n; ++node) {
            if (findRoot(parent, node) == target) {
                result.push_back(node);
            }
        }

        return result;
    }

private:
    int findRoot(vector<int>& parent, int node) {
        int root = node;

        // 找到根
        while (parent[root] != root) {
            root = parent[root];
        }

        // 路径压缩
        while (parent[node] != node) {
            int next = parent[node];
            parent[node] = root;
            node = next;
        }

        return root;
    }
};

