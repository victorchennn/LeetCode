package Companies.Amazon;

import Libs.ListNode;

public class SwapNodesinPairs {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode tail = dummy;
        while (tail.next != null && tail.next.next != null) {
            ListNode first = tail.next;
            ListNode second = tail.next.next;

            tail.next = second;
            first.next = second.next;
            second.next = first;

            tail = first;
        }
        return dummy.next;
    }
}
