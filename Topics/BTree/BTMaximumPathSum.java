package Topics.BTree;

import Libs.TreeNode;

/**
 * Time complexity: O(N), where N is number of nodes, since we visit each node not more than 2 times.
 * Space complexity: O(log(N)), we have to keep a recursion stack of the size of the tree height.
 */
public class BTMaximumPathSum {
    int max = Integer.MIN_VALUE;

    public int maxPathSum(TreeNode root) {
        helper(root);
        return max;
    }

    private int helper(TreeNode root) {
        if (root == null) {
            return 0;
        }
        int left = Math.max(0, helper(root.left));
        int right = Math.max(0, helper(root.right));
        max = Math.max(max, left+right+root.val);
        return root.val + Math.max(left, right);
    }
}


class Solution {
public:
    int maxPathSum(TreeNode* root) {
        dfs(root);
        return maxSum_;
    }

private:
    int maxSum_ = INT_MIN;

    int dfs(TreeNode* root) {
        if (!root) {
            return 0;
        }

        int left = max(0, dfs(root->left));
        int right = max(0, dfs(root->right));

        maxSum_ = max(maxSum_, left + right + root->val); // left + right + root 用来更新全局答案，因为路径可以在当前节点拐弯

        return root->val + max(left, right); // root + max(left, right) 用来返回给父节点，因为返回的路径必须保持一条链，不能同时走左右两个子树。
    }
};

class Solution {
public:
    int maxPathSum(TreeNode* root) {
        return dfs(root).best;
    }

private:
    struct Result {
        int gain;
        int best;
    };

    Result dfs(TreeNode* root) {
        if (!root) {
            return {0, INT_MIN};
        }

        Result left = dfs(root->left);
        Result right = dfs(root->right);

        int gain = root->val + max(0, max(left.gain, right.gain));

        int throughRoot =
            root->val +
            max(0, left.gain) +
            max(0, right.gain);

        int best = max({
            left.best,
            right.best,
            throughRoot
        });

        return {gain, best};
    }
};
