class Codec {
public:
    string serialize(TreeNode* root) {
        string result;
        preorder(root, result);

        if (!result.empty()) {
            result.pop_back();   // 去掉最后一个 ','
        }

        return result;
    }

    TreeNode* deserialize(string data) {
        if (data.empty()) {
            return nullptr;
        }

        TreeNode* root = nullptr;

        stringstream ss(data);
        string token;

        while (getline(ss, token, ',')) {
            root = insert(root, stoi(token));
        }

        return root;
    }

private:
    void preorder(TreeNode* root, string& result) {
        if (!root) {
            return;
        }

        result += to_string(root->val) + ",";

        preorder(root->left, result);
        preorder(root->right, result);
    }

    TreeNode* insert(TreeNode* root, int val) {
        if (!root) {
            return new TreeNode(val);
        }

        if (val < root->val) {
            root->left = insert(root->left, val);
        } else {
            root->right = insert(root->right, val);
        }

        return root;
    }
};
