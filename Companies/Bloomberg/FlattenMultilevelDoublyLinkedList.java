package Companies.Bloomberg;

import Libs.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public class FlattenMultilevelDoublyLinkedList {
    public Node flatten(Node head) {
      if (head == null) return head;

        Node pseudoHead = new Node(0, null, head, null);
        Node curr, prev = pseudoHead;

        Deque<Node> stack = new ArrayDeque<>();
        stack.push(head);

        while (!stack.isEmpty()) {
            curr = stack.pop();
            prev.next = curr;
            curr.prev = prev;

            if (curr.next != null) stack.push(curr.next);
            if (curr.child != null) {
                stack.push(curr.child);
                curr.child = null;
            }
            prev = curr;
        }
        pseudoHead.next.prev = null;
        return pseudoHead.next;

//        helper(head);
//        return head;
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
