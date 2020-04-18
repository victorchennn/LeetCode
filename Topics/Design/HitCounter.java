package Topics.Design;

import java.util.ArrayDeque;
import java.util.Deque;

public class HitCounter {
    Deque<int[]> q;
    int size;

    /** Initialize your data structure here. */
    public HitCounter() {
        q = new ArrayDeque<>();
    }

    /** Record a hit.
     @param timestamp - The current timestamp (in seconds granularity). */
    public void hit(int timestamp) {
        if (q.isEmpty() || q.peekLast()[0] != timestamp) {
            q.offer(new int[]{timestamp, 1});
        } else {
            q.peekLast()[1]++;
        }
        size++;
    }

    /** Return the number of hits in the past 5 minutes.
     @param timestamp - The current timestamp (in seconds granularity). */
    public int getHits(int timestamp) {
        while (!q.isEmpty() && q.peekFirst()[0]+300 <= timestamp) {
            size -= q.pollFirst()[1];
        }
        return size;
    }
}
