class Solution {
public:
    TreeNode* findNode(TreeNode* root, unsigned int target) {
        if (root == nullptr || target == 0) {
            return nullptr;
        }

        if (target == 1) {
            return root;
        }

        TreeNode* parent = findNode(root, target / 2);

        if (parent == nullptr) {
            return nullptr;
        }

        if (target % 2 == 0) {
            return parent->left;
        }

        return parent->right;
    }
};

struct TreeNode {
    int val;
    TreeNode* left;
    TreeNode* right;

    explicit TreeNode(int value)
        : val(value), left(nullptr), right(nullptr) {}
};

class Solution {
public:
    bool exists(TreeNode* root, unsigned int target) {
        if (root == nullptr || target == 0) {
            return false;
        }

        // 找到 target 最高位下面的第一位
        unsigned int mask = highestBit(target) >> 1;

        TreeNode* current = root;

        while (mask != 0 && current != nullptr) {
            if ((target & mask) == 0) {
                current = current->left;
            } else {
                current = current->right;
            }

            mask >>= 1;
        }

        return current != nullptr;
    }

private:
    unsigned int highestBit(unsigned int n) {
        unsigned int bit = 1;

        while (bit <= n / 2) {
            bit <<= 1;
        }

        return bit;
    }
};
