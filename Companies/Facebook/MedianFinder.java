package Companies.Facebook;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class MedianFinder {
    private List<Integer> l;

    /** initialize your data structure here. */
    public MedianFinder() {
        l = new ArrayList<>();
    }

    public void addNum(int num) {
        if (l.size() == 0) {
            l.add(num);
        } else {
            helper(num, 0, l.size());
        }
    }

    public double findMedian() {
        int len = l.size();
        if (len % 2 == 1) {
            return l.get(len/2) / 1.0;
        } else {
            return (l.get(len/2) + l.get(len/2-1)) / 2.0;
        }
    }

    private void helper(int num, int left, int right) {
        if (left == right) {
            l.add(left, num);
        } else {
            int index = left + (right-left)/2;
            int median = l.get(index);
            if (median == num) {
                l.add(index, num);
            } else if (median > num) {
                helper(num, left, index);
            } else {
                helper(num, index+1, right);
            }
        }
    }

    private Queue<Integer> small = new PriorityQueue<>((a,b)->b-a);
    private Queue<Integer> large = new PriorityQueue<>();

//    public void addNum(int num) {
//        large.add(num);
//        small.add(large.poll());
//        if (large.size() < small.size())
//            large.add(small.poll());
//    }
//
//
//    public double findMedian() {
//        return large.size() > small.size()
//                ? large.peek()
//                : (large.peek() + small.peek()) / 2.0;
//    }
}
