package Topics.BTree;

import Libs.Node;
import Libs.TreeNode;

import java.util.ArrayList;

public class NaryTreeEncodeToBT {

    // Encodes an n-ary tree to Companies.Amazon binary tree.
    public TreeNode encode(Node root) {
        if (root == null) {
            return null;
        }
        TreeNode re = new TreeNode(root.val);
        if (root.children.size() > 0) {
            re.left = encode(root.children.get(0));
        }
        TreeNode cur = re.left;
        for (int i = 1; i < root.children.size(); i++) {
            cur.right = encode(root.children.get(i));
            cur = cur.right;
        }
        return re;
    }

    // Decodes your binary tree to an n-ary tree.
    public Node decode(TreeNode root) {
        if (root == null) {
            return null;
        }
        Node re = new Node(root.val, new ArrayList<>());
        TreeNode cur = root.left;
        while (cur != null) {
            re.children.add(decode(cur));
            cur = cur.right;
        }
        return re;
    }
}
