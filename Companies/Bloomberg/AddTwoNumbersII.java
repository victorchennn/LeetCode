package Companies.Bloomberg;

import Libs.ListNode;

import java.util.Stack;

public class AddTwoNumbersII {
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<ListNode> s1 = new Stack<>();
        Stack<ListNode> s2 = new Stack<>();
        while (l1 != null) {
            s1.push(l1);
            l1 = l1.next;
        }
        while (l2 != null) {
            s2.push(l2);
            l2 = l2.next;
        }
        ListNode re = null;
        int carry = 0;
        while (!s1.isEmpty() || !s2.isEmpty() || carry != 0) {
            int x = s1.isEmpty()?0:s1.pop().val;
            int y = s2.isEmpty()?0:s2.pop().val;
            int sum = x + y + carry;
            ListNode temp = new ListNode(sum%10);
            carry = sum/10;
            temp.next = re;
            re = temp;
        }
        return re;
    }

    public ListNode addTwoNumbersII(ListNode l1, ListNode l2) {
        int len1 = getLen(l1), len2 = getLen(l2);
        ListNode re = new ListNode(1);
        re.next = len1 > len2? helper(l1, l2, len1-len2):helper(l2, l1, len2-len1);
        if (re.next.val > 9) {
            re.next.val %= 10;
            return re;
        }
        return re.next;
    }

    private int getLen(ListNode l) {
        int count = 0;
        while(l != null) {
            l = l.next;
            count++;
        }
        return count;
    }

    private ListNode helper(ListNode l1, ListNode l2, int diff) {
        if (l1 == null) {
            return null;
        }
        ListNode re = diff == 0? new ListNode(l1.val+l2.val):new ListNode(l1.val);
        re.next = diff == 0? helper(l1.next, l2.next, 0):helper(l1.next, l2, diff-1);
        if (re.next != null && re.next.val > 9) {
            re.next.val %= 10;
            re.val += 1;
        }
        return re;
    }
}
