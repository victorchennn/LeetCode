class Solution {
public:
    vector<vector<int>> pathSum(TreeNode* root, int targetSum) {
        vector<vector<int>> result;

        if (root == nullptr) {
            return result;
        }

        vector<int> path;
        path.push_back(root->val);

        dfs(root, targetSum - root->val, path, result);

        return result;
    }

private:
    void dfs(TreeNode* node, int remainingSum, vector<int>& path, vector<vector<int>>& result) {
        if (node->left == nullptr && node->right == nullptr) {
            if (remainingSum == 0) {
                result.push_back(path);
            }
            return;
        }

        if (node->left != nullptr) {
            path.push_back(node->left->val);
            dfs(node->left, remainingSum - node->left->val, path, result);
            path.pop_back();
        }

        if (node->right != nullptr) {
            path.push_back(node->right->val);
            dfs(node->right, remainingSum - node->right->val, path, result);
            path.pop_back();
        }
    }
};
