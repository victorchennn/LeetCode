class Solution {
public:
    bool hasPathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return false;
        }

        targetSum -= root->val;

        if (root->left == nullptr &&
            root->right == nullptr) {
            return targetSum == 0;
        }

        return hasPathSum(root->left, targetSum) ||
               hasPathSum(root->right, targetSum);
    }
};

class Solution {
public:
    bool hasPathSum(TreeNode* root, int targetSum) {
        if (root == nullptr) {
            return false;
        }

        std::stack<TreeNode*> nodeStack;
        std::stack<int> sumStack;

        nodeStack.push(root);
        sumStack.push(targetSum - root->val);

        while (!nodeStack.empty()) {
            TreeNode* node = nodeStack.top();
            nodeStack.pop();

            int remain = sumStack.top();
            sumStack.pop();

            if (node->left == nullptr && node->right == nullptr && remain == 0) {
                return true;
            }
            if (node->right) {
                nodeStack.push(node->right);
                sumStack.push(remain - node->right->val);
            }
            if (node->left) {
                nodeStack.push(node->left);
                sumStack.push(remain - node->left->val);
            }
        }

        return false;
    }
};
