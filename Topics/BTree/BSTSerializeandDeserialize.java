package Topics.BTree;

import Libs.TreeNode;

public class BSTSerializeandDeserialize {
    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        if (root == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        helperS(sb, root);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    private void helperS(StringBuilder sb, TreeNode root) {
        if (root == null) {
            return;
        }
        sb.append(root.val + ",");
        helperS(sb, root.left);
        helperS(sb, root.right);
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        if (data.length() == 0) {
            return null;
        }
        TreeNode root = null;
        for (String s : data.split(",")) {
            root = helperD(root, Integer.valueOf(s));
        }
        return root;
    }

    private TreeNode helperD(TreeNode root, int val) {
        if (root == null) {
            return new TreeNode(val);
        }
        if (val < root.val) {
            root.left = helperD(root.left, val);
        } else {
            root.right = helperD(root.right, val);
        }
        return root;
    }
}
