class Solution {
public:
    int pathSum(TreeNode* root, int targetSum) {
        std::unordered_map<long long, int> prefix;
        prefix[0] = 1;
        return dfs(root, 0, targetSum, prefix);
    }

private:
    int dfs(TreeNode* node, long long curr, int target, std::unordered_map<long long, int>& prefix) {
        if (!node) {
            return 0;
        }

        curr += node->val;
        int ans = prefix[curr - target];

        ++prefix[curr];

        ans += dfs(node->left, curr, target, prefix);
        ans += dfs(node->right, curr, target, prefix);

        --prefix[curr];

        return ans;
    }
};
