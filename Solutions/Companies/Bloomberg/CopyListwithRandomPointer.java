package Companies.Bloomberg;

import Libs.Node;

import java.util.HashMap;
import java.util.Map;

public class CopyListwithRandomPointer {
    public Node copyRandomList(Node head) {
        if (head == null) {
            return null;
        }
        Node copy = head;
        while (copy != null) {
            Node newone = new Node(copy.val);
            newone.next = copy.next;
            copy.next = newone;
            copy = newone.next;
        }
        copy = head;
        while (copy != null) {
            copy.next.random = copy.random == null? null : copy.random.next;
            copy = copy.next.next;
        }
        Node re = head.next;
        Node newhead = head.next;
        Node oldhead = head;
        while (oldhead != null) {
            oldhead.next = oldhead.next.next;
            newhead.next = newhead.next == null? null : newhead.next.next;
            oldhead = oldhead.next;
            newhead = newhead.next;
        }
        return re;
    }

    private Map<Node, Node> visitedHash = new HashMap<>();

    public Node copyRandomListII(Node head) {
        if (head == null) {
            return null;
        }
        if (visitedHash.containsKey(head)) {
            return visitedHash.get(head);
        }
        Node node = new Node(head.val);
        visitedHash.put(head, node);
        node.next = copyRandomListII(head.next);
        node.random = copyRandomListII(head.random);
        return node;
    }
}
