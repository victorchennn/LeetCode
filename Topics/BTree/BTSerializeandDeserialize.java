package Topics.BTree;

import Libs.TreeNode;

import java.util.*;

public class BTSerializeandDeserialize {
    private String SPLIT = ",";
    private String NULL = "X";

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        StringBuilder sb = new StringBuilder();
        helperSerialize(sb, root);
        return sb.toString();
    }

    private void helperSerialize(StringBuilder sb, TreeNode root) {
        if (root == null) {
            sb.append(NULL).append(SPLIT);
        } else {
            sb.append(root.val).append(SPLIT);
            helperSerialize(sb, root.left);
            helperSerialize(sb, root.right);
        }
    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        Queue<String> q = new LinkedList<>();
        q.addAll(Arrays.asList(data.split(SPLIT)));
        return helperDeserialize(q);
    }

    private TreeNode helperDeserialize(Queue<String> q) {
        String head = q.poll();
        if (head.equals(NULL)) {
            return null;
        } else {
            TreeNode root = new TreeNode(Integer.valueOf(head));
            root.left = helperDeserialize(q);
            root.right = helperDeserialize(q);
            return root;
        }
    }
}
