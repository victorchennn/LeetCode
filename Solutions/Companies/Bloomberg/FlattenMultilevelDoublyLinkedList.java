package Companies.Bloomberg;

import Libs.Node;

public class FlattenMultilevelDoublyLinkedList {
    public Node flatten(Node head) {
//        Node cur = head;
//        while (cur != null) {
//            if (cur.child == null) {
//                cur = cur.next;
//                continue;
//            }
//            Node tail = cur.child;
//            while (tail.next != null) {
//                tail = tail.next;
//            }
//            tail.next = cur.next;
//            if (cur.next != null) {     // Be careful!
//                cur.next.prev = tail;
//            }
//            cur.next = cur.child;
//            cur.child.prev = cur;
//            cur.child = null;
//            cur = cur.next;
//        }
//        return head;

        helper(head);
        return head;
    }

    private Node helper(Node node) {
        if (node == null) {
            return null;
        }
        if (node.child == null) {
            if (node.next == null) {
                return node;
            } else {
                return helper(node.next);
            }
        }
        Node tail = helper(node.child);
        tail.next = node.next;
        if (node.next != null) {
            node.next.prev = tail;
        }
        node.next = node.child;
        node.child.prev = node;
        node.child = null;
        return helper(node.next);
    }
}
