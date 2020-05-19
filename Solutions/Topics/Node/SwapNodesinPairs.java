package Topics.Node;

import Libs.ListNode;

public class SwapNodesinPairs {
    public ListNode swapPairs(ListNode head) {
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode tail = dummy, first, second;
        while (tail.next != null && tail.next.next != null) {
            first = tail.next;
            second = tail.next.next;

            tail.next = second;
            first.next = second.next;
            second.next = first;

            tail = first;
        }
        return dummy.next;
    }
}
