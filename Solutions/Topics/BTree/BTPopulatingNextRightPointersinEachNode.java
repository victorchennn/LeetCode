package Topics.BTree;

import Libs.Node;

public class BTPopulatingNextRightPointersinEachNode {
    public Node connect(Node root) {
        Node head = root;
        while (head != null) {
            Node cur = head;
            while (cur != null) {
                if (cur.left != null) {
                    cur.left.next = cur.right;
                }
                if (cur.right != null && cur.next != null) {
                    cur.right.next = cur.next.left;
                }
                cur = cur.next;
            }
            head = head.left;
        }
        return root;
    }
}
