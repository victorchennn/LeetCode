package Companies.Google;

import Libs.ListNode;

public class PlusOneLinkedList {
    public ListNode plusOne(ListNode head) {
        if (dfs(head) == 0) {
            return head;
        } else {
            ListNode newhead = new ListNode(1);
            newhead.next = head;
            return newhead;
        }
    }

    private int dfs(ListNode head) {
        if (head == null) {
            return 1;
        }
        int carry = dfs(head.next);
        if (carry == 0) {
            return 0;
        }
        int val = head.val + carry;
        head.val = val%10;
        return val/10;
    }
}
