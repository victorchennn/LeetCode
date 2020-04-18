package Companies.Microsoft;

import Libs.ListNode;

public class RotateList {
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode slow = dummy, fast = dummy;
        int len = 0;
        while (fast.next != null) {
            fast = fast.next;
            len++;
        }
        k = len - k%len;
        while (k > 0) {
            slow = slow.next;
            k--;
        }
        fast.next = dummy.next;
        dummy.next = slow.next;
        slow.next = null;
        return dummy.next;
    }
}
