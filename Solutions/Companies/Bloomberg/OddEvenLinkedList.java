package Companies.Bloomberg;

import Libs.ListNode;

public class OddEvenLinkedList {
    public ListNode oddEvenList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        int len = 1;
        ListNode tail = head, cur = head;
        while (tail.next != null) {
            len++;
            tail = tail.next;
        }
        int i = len/2;
        while (i-- > 0) {
            tail.next = cur.next;
            cur.next = cur.next.next;
            tail = tail.next;
            cur = cur.next;
        }
        tail.next = null;
        return head;
    }
}
