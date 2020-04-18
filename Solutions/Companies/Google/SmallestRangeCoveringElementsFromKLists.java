package Companies.Google;

import java.util.List;
import java.util.PriorityQueue;

public class SmallestRangeCoveringElementsFromKLists {
    public int[] smallestRange(List<List<Integer>> nums) {
        PriorityQueue<int[]> q = new PriorityQueue<>(
                (a, b)-> nums.get(a[0]).get(a[1]) - nums.get(b[0]).get(b[1]));
        int start = 0, end = Integer.MAX_VALUE;
        int max = 0;
        for (int i = 0; i < nums.size(); i++) {
            q.add(new int[]{i, 0});
            max = Math.max(max, nums.get(i).get(0));
        }
        while (q.size() == nums.size()) {
            int[] cur = q.poll();
            int row = cur[0], col = cur[1];
            if (end - start > max - nums.get(row).get(col)) {
                start = nums.get(row).get(col);
                end = max;
            }
            if (col + 1 < nums.get(row).size()) {
                q.add(new int[] {row, col+1});
                max = Math.max(max, nums.get(row).get(col+1));
            }
        }
        return new int[]{start, end};
    }
}
