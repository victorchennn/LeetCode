package Companies.GoldmanSachs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HighFive {

    /**
     * Input: [[1,91],[1,92],[2,93],[2,97],[1,60],[2,77],[1,65],[1,87],[1,100],[2,100],[2,76]]
     * Output: [[1,87],[2,88]]
     */
    public int[][] highFive(int[][] items) {
        Arrays.sort(items, (a, b) -> a[0] != b[0]? a[0]-b[0]:b[1]-a[1]);
        List<int[]> re = new ArrayList<>();
        for (int i = 0; i < items.length; ) {
            if (i == 0 || items[i][0] != items[i-1][0]) {
                int sum = 0;
                int id = items[i][0];
                for (int j = 0; j < 5; j++) {
                    sum += items[i++][1];
                }
                re.add(new int[]{id, sum/5});
            } else {
                i++;
            }
        }
        return re.toArray(new int[0][]);
    }
}
