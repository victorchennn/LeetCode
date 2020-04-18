package Companies.Google;

public class FlattenAMultilevelDoublyLinkedList {
    public Node flatten(Node head) {
        Node cur = head;
        while (cur != null) {
            if (cur.child == null) {
                cur = cur.next;
                continue;
            }
            Node child = cur.child;
            while (child.next != null) {
                child = child.next;
            }
            child.next = cur.next;
            if (cur.next != null) {        // be careful!
                cur.next.prev = child;
            }
            cur.next = cur.child;
            cur.child.prev = cur;
            cur.child = null;
            cur = cur.next;
        }
        return head;
    }

    class Node {
        public int val;
        public Node prev;
        public Node next;
        public Node child;

        public Node() {
        }

        public Node(int _val, Node _prev, Node _next, Node _child) {
            val = _val;
            prev = _prev;
            next = _next;
            child = _child;
        }
    }
}
