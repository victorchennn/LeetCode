class Solution { // BFS
public:
    vector<vector<int>> levelOrder(TreeNode* root) {
        vector<vector<int>> result;
        if (!root) {
            return result;
        }

        queue<TreeNode*> q;
        q.push(root);

        while (!q.empty()) {
            int size = q.size();
            result.emplace_back();

            for (int i = 0; i < size; ++i) {
                TreeNode* cur = q.front();
                q.pop();

                result.back().push_back(cur->val);

                if (cur->left) {
                    q.push(cur->left);
                }
                if (cur->right) {
                    q.push(cur->right);
                }
            }
        }

        return result;
    }
};

class Solution { // DFS
public:
    vector<vector<int>> levelOrder(TreeNode* root) {
        vector<vector<int>> result;
        dfs(root, 0, result);
        return result;
    }

private:
    void dfs(TreeNode* node, int level, vector<vector<int>>& result) {
        if (!node) {
            return;
        }

        if (level == result.size()) {
            result.emplace_back();
        }

        result[level].push_back(node->val);

        dfs(node->left, level + 1, result);
        dfs(node->right, level + 1, result);
    }
};
