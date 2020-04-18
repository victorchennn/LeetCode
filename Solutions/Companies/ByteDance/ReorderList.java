package Companies.ByteDance;

import Libs.ListNode;

public class ReorderList {
    public void reorderList(ListNode head) {
        if (head == null) {
            return;
        }
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        ListNode half = reverse(slow.next);
        slow.next = null;
        while (half != null) {
            ListNode t1 = head.next;
            ListNode t2 = half.next;
            head.next = half;
            half.next = t1;
            head = t1;
            half = t2;
        }
    }

    private ListNode reverse(ListNode head) {
        ListNode re = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = re;
            re = head;
            head = next;
        }
        return re;
    }
}
