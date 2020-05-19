package Topics.Node;

import Libs.Node;

import java.util.ArrayDeque;
import java.util.Deque;

public class FlattenMultilevelDoublyLinkedList {
    public Node flatten(Node head) {
        if (head == null) {
            return null;
        }
        Node pseudoHead = new Node(-1);
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

        /* may visit nodes twice */
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
//            if (cur.next != null) {
//                cur.next.prev = tail;
//            }
//            cur.next = cur.child;
//            cur.child.prev = cur;
//            cur.child = null;
//            cur = cur.next;
//        }
//        return head;

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
        return helper(tail);
    }
}
