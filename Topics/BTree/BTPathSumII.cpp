// Given the root of a BT and an integer targetSum, 
// return all root-to-leaf paths where the sum of the node values in the path equals targetSum. 
// Each path should be returned as a list of the node values, not node references.

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

// 如果你想改成路径和最小的 root-to-leaf 路径，不需要 backtracking 保存所有答案，只要 DFS 返回左右子树中较小的路径和：
class Solution {
public:
    vector<int> minPath(TreeNode* root) {
        vector<int> path;
        if (!root) return path;
        dfs(root, path);
        return path;
    }

private:
    int dfs(TreeNode* node, vector<int>& path) {
        if (!node->left && !node->right) {
            path = {node->val};
            return node->val;
        }

        vector<int> leftPath, rightPath;
        int left = INT_MAX, right = INT_MAX;

        if (node->left) left = dfs(node->left, leftPath);
        if (node->right) right = dfs(node->right, rightPath);

        path = (left <= right) ? leftPath : rightPath;
        path.insert(path.begin(), node->val);

        return node->val + min(left, right);
    }
};
