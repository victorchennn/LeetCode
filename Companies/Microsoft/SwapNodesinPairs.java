package Companies.Microsoft;

import Libs.ListNode;

public class SwapNodesinPairs {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode t = dummy;
        while (t.next != null && t.next.next != null) {
            ListNode first = t.next;
            ListNode second = t.next.next;

            t.next = second;
            first.next = second.next;
            second.next = first;

            t = first;
        }

        return dummy.next;
    }
}
