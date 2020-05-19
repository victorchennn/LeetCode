package Topics.BTree;

import Libs.Node;

import java.util.LinkedList;
import java.util.Queue;

public class BTPopulatingNextRightPointersinEachNodeII {
    public Node connect(Node root) {
        Node cur = root, head = null, prev = null;
        while (cur != null) {
            while (cur != null) {
                if (cur.left != null) {
                    if (prev != null) {
                        prev.next = cur.left;
                    } else {
                        head = cur.left;
                    }
                    prev = cur.left;
                }
                if (cur.right != null) {
                    if (prev != null) {
                        prev.next = cur.right;
                    } else {
                        head = cur.right;
                    }
                    prev = cur.right;
                }
                cur = cur.next;
            }
            cur = head;
            head = null;
            prev = null;
        }
        return root;
    }

    public Node connectII(Node root) {
        if (root == null) {
            return null;
        }
        Queue<Node> Q = new LinkedList<>();
        Q.add(root);
        while (Q.size() > 0) {
            int size = Q.size();
            for(int i = 0; i < size; i++) {
                Node node = Q.poll();
                if (i < size - 1) {
                    node.next = Q.peek();
                }
                if (node.left != null) {
                    Q.add(node.left);
                }
                if (node.right != null) {
                    Q.add(node.right);
                }
            }
        }
        return root;
    }
}
