package Companies.Facebook;

import Libs.ListNode;

public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        return merge(lists, 0, lists.length-1);
    }

    private ListNode merge(ListNode[] lists, int l, int r) {
        if (l == r) {
            return lists[l];
        }
        int m = l + (r-l)/2;
        ListNode left = merge(lists, l, m);
        ListNode right = merge(lists, m+1, r);
        return helper(left, right);
    }

    private ListNode helper(ListNode l, ListNode r) {
        ListNode re = new ListNode(-1);
        ListNode tail = re;
        while (l != null && r != null) {
            if (l.val < r.val) {
                tail.next = l;
                l = l.next;
            } else {
                tail.next = r;
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
        return re.next;
    }
}
