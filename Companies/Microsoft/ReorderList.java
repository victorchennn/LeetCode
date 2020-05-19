package Companies.Microsoft;

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
            ListNode l1 = head.next;
            ListNode l2 = half.next;
            head.next = half;
            half.next = l1;
            head = l1;
            half = l2;
        }
    }

    private ListNode reverse(ListNode node) {
        ListNode re = null;
        while (node != null) {
            ListNode temp = node.next;
            node.next = re;
            re = node;
            node = temp;
        }
        return re;
    }
}
