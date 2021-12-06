package Companies.Google;

import java.util.Arrays;
import java.util.PriorityQueue;

public class SingleThreadedCPU {
    public int[] getOrder(int[][] tasks) {
        int len = tasks.length;
        int[] re = new int[len];
        int[][] events = new int[len][3];
        for (int i = 0; i < len; i++) {
            events[i][0] = i;
            events[i][1] = tasks[i][0];
            events[i][2] = tasks[i][1];
        }
        Arrays.sort(events, (a,b) -> a[1]-b[1]);
        PriorityQueue<int[]> pq = new PriorityQueue<>(
                (a,b) -> a[2]==b[2]? a[0]-b[0]:a[2]-b[2]); // has to compare processing time first
        int time = 0, i = 0, ei = 0;
        while (i < len) {
            while (ei < len && events[ei][1] <= time) {
                pq.offer(events[ei++]);
            }
            if (pq.isEmpty()){
                time = events[ei][1];
                continue;
            }
            int[] task = pq.poll();
            re[i++] = task[0];
            time += task[2];
        }
        return re;
    }
}
