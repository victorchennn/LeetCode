package Companies.Twitter;

import java.util.Arrays;
import java.util.PriorityQueue;

public class CareerFair {
    private int maxEvents(int[] arrival, int[] duration) {
        int[][] events = new int[arrival.length][2];
        for(int i=0;i<arrival.length;i++) {
            events[i] = new int[] {arrival[i], arrival[i] + duration[i]};
        }
        Arrays.sort(events, (a, b)->(a[1] - b[1]));
        PriorityQueue<int[]> q = new PriorityQueue<>((a, b)->a[1] - b[1]);
        int[] first = events[0];
        for(int i = 1; i < events.length; i++) {
            int[] cur = events[i];
            if(cur[0] < first[1])
                q.offer(cur);
            else {
                first[1] = events[i][1];
            }
        }
        return arrival.length - q.size();
    }

    public static void main(String...args) {
        CareerFair test = new CareerFair();
        System.out.println(test.maxEvents(new int[]{1,3,3,5,7}, new int[]{2,2,1,2,1}));
    }

}
