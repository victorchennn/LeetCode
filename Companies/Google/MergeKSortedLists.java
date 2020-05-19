package Companies.Google;

import Libs.ListNode;

import java.util.PriorityQueue;

public class MergeKSortedLists {
    public ListNode mergeKLists(ListNode[] lists) {
        if (lists.length == 0) {
            return null;
        }
        return mergeSort(lists, 0, lists.length-1);
    }

    private ListNode mergeSort(ListNode[] lists, int l, int r) {
        if (l == r) {
            return lists[l];
        }
        int m = l + (r-l)/2;
        ListNode left = mergeSort(lists, l, m); // l,m
        ListNode right = mergeSort(lists, m+1, r); // m+1,r
        return merge(left, right);
    }

    private ListNode merge(ListNode l, ListNode r) {
        ListNode re = new ListNode(0);
        ListNode tail = re;
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
        return re.next;
    }

    public ListNode mergeKListsII(ListNode[] lists) {
        PriorityQueue<ListNode> p = new PriorityQueue<>((a,b)->a.val-b.val);
        for (ListNode l : lists) {
            if (l != null) {
                p.add(l);
            }
        }
        ListNode re = new ListNode(0);
        ListNode tail = re;
        while (!p.isEmpty()) {
            ListNode cur = p.poll();
            tail.next = new ListNode(cur.val);
            cur = cur.next;
            tail = tail.next;
            if (cur != null) {
                p.add(cur);
            }
        }
        return re.next;
    }
}
