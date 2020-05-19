package Topics.Node;

import Libs.ListNode;

public class PalindromeLinkedList {
    public boolean isPalindrome(ListNode head) {
        ListNode slow = head, fast = head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        if (fast != null) {
            slow = slow.next;
        }
        slow = reverse(slow);
        while (slow != null) {
            if (slow.val != head.val) {
                return false;
            }
            slow = slow.next;
            head = head.next;
        }
        return true;
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
