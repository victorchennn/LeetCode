package Companies.ByteDance;

import Libs.ListNode;

public class ReverseLinkedList {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode p = reverseList(head.next);
        head.next.next = head;
        head.next = null;
        return p;
    }

    public ListNode reverseListII(ListNode head) {
        ListNode re = null;
        while (head != null) {
            ListNode temp = head.next;
            head.next = re;
            re = head;
            head = temp;
        }
        return re;
    }
}
