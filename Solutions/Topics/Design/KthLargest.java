package Topics.Design;

import java.util.PriorityQueue;

public class KthLargest {
    PriorityQueue<Integer> q;
    int k;

    public KthLargest(int k, int[] nums) {
        q = new PriorityQueue<>(k);
        this.k = k;
        for (int num : nums) {
            add(num); // add not q.add;
        }
    }

    public int add(int val) {
        if (q.size() < k) {
            q.add(val);
        } else if (val > q.peek()) {
            q.poll();
            q.add(val);
        }
        return q.peek();
    }
}
