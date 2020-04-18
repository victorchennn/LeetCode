package Companies.Microsoft;

import Libs.Node;

public class FlattenMultilevelDoublyLinkedList {
    public Node flatten(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.child == null) {
                cur = cur.next;
                continue;
            }
            Node tail = cur.child;
            while (tail.next != null) {
                tail = tail.next;
            }
            tail.next = cur.next;
            if (cur.next != null) {     // Be careful!
                cur.next.prev = tail;
            }
            cur.next = cur.child;
            cur.child.prev = cur;
            cur.child = null;
            cur = cur.next;
        }
        return head;
    }
}
