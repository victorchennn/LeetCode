package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Sort [[7,0], [4,4], [7,1], [5,0], [6,1], [5,2]]
 * To   [[7,0], [7,1], [6,1], [5,0], [5,2], [4,4]]
 */
public class QueueReconstructionbyHeight {
    public int[][] reconstructQueue(int[][] people) {
        Arrays.sort(people, (a, b)->a[0]==b[0]?a[1]-b[1]:b[0]-a[0]);
        List<int[]> l = new ArrayList<>();
        for (int[] p : people) {
            l.add(p[1], p);
        }
        return l.toArray(new int[people.length][2]);
    }

}
