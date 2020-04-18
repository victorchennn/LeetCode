package Companies.Google;

import Libs.Node;

public class PopulatingNextRightPointersInEachNode {
    public Node connect(Node root) {
        Node level = root;
        while (level != null) {
            Node cur = level;
            while (cur != null) {
                if (cur.left != null) {
                    cur.left.next = cur.right;
                }
                if (cur.right != null && cur.next != null) {
                    cur.right.next = cur.next.left;
                }
                cur = cur.next;
            }
            level = level.left;
        }
        return root;
    }
}
