package Companies.Microsoft;

import Libs.ListNode;

public class LinkedListCycle {
    public ListNode detectCycle(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                ListNode count = head;
                while (count != slow) {
                    count = count.next;
                    slow = slow.next;
                }
                return slow;
            }
        }
        return null;

    }
}
