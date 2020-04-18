package Companies.Google;

import Libs.ListNode;
import Libs.TreeNode;

public class ConvertSortedListToBST {
    public TreeNode sortedListToBST(ListNode head) {
        if (head == null) {
            return null;
        }
        if (head.next == null) {
            return new TreeNode(head.val);
        }
        ListNode slow = head, fast = head, prev = null;
        while (fast != null && fast.next != null) {
            prev = slow;
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode re = new TreeNode(slow.val);
        prev.next = null;
        re.left = sortedListToBST(head);
        re.right = sortedListToBST(slow.next);
        return re;
    }
}
