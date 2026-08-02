class Codec {
public:
    string serialize(TreeNode* root) {
        string result;
        preorder(root, result);
        return result;
    }

    TreeNode* deserialize(string data) {
        queue<string> q;

        stringstream ss(data);
        string token;

        while (getline(ss, token, ',')) {
            q.push(token);
        }

        return build(q);
    }

private:
    void preorder(TreeNode* root, string& result) {
        if (!root) {
            result += "X,";
            return;
        }

        result += to_string(root->val) + ",";

        preorder(root->left, result);
        preorder(root->right, result);
    }

    TreeNode* build(queue<string>& q) {
        string cur = q.front();
        q.pop();

        if (cur == "X") {
            return nullptr;
        }

        TreeNode* root = new TreeNode(stoi(cur));

        root->left = build(q);
        root->right = build(q);

        return root;
    }
};
