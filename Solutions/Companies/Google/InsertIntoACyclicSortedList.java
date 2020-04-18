package Companies.Google;

import Libs.Node;

public class InsertIntoACyclicSortedList {
    public Node insert(Node head, int insertVal) {
        if (head == null) {
            Node re = new Node(insertVal);
            re.next = re;
            return re;
        }
        Node tail = head;
        while (tail.val <= tail.next.val && tail.next != head) {
            tail = tail.next;
        }
        Node dummy = new Node(0, tail.next);
        tail.next = null;
        Node find = dummy;
        while (find.next != null && find.next.val < insertVal) {
            find = find.next;
        }
        find.next = new Node(insertVal, find.next);
        tail = tail.next == null? tail:tail.next;
        tail.next = dummy.next;
        return head;
    }
}
