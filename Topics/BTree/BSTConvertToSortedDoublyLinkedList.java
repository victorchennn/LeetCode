package Topics.BTree;

import Libs.Node;

public class BSTConvertToSortedDoublyLinkedList {
    private Node head = null, tail = null;

    public Node treeToDoublyList(Node root) {
        if (root == null) {
            return null;
        }
        helper(root);
        head.left = tail;
        tail.right = head;
        return head;
    }

    private void helper(Node root) {
        if (root != null) {
            helper(root.left);
            if (tail == null) {
                head = root;
            } else {
                tail.right = root;
                root.left = tail;
            }
            tail = root;
            helper(root.right);
        }
    }
}
