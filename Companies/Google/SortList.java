package Companies.Google;

import Libs.ListNode;

public class SortList {
    public ListNode sortList(ListNode head) {
        if (head == null || head.next == null) {
            return head;
        }
        ListNode prev = null, slow = head, fast = head;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        prev.next = null;
        ListNode left = sortList(head);
        ListNode right = sortList(slow);
        return merge(left, right);
    }

    private ListNode merge(ListNode l, ListNode r) {
        ListNode dummy = new ListNode(-1);
        ListNode tail = dummy;
        while (l != null && r != null) {
            if (l.val < r.val) {
                tail.next = new ListNode(l.val);
                l = l.next;
            } else {
                tail.next = new ListNode(r.val);
                r = r.next;
            }
            tail = tail.next;
        }
        if (l != null) {
            tail.next = l;
        }
        if (r != null) {
            tail.next = r;
        }
        return dummy.next;
    }
}
