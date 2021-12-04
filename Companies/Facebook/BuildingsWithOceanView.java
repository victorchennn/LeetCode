package Companies.Facebook;

import java.util.ArrayList;
import java.util.List;

public class BuildingsWithOceanView {
    public int[] findBuildings(int[] heights) {
        int length = heights.length;
        List<Integer> l = new ArrayList<>();
        int max = -1;
        for (int i = length-1; i >= 0; i--) {
            if (heights[i] > max) {
                l.add(i);
                max = heights[i];
            }
        }
        int[] re = new int[l.size()];
        for (int i = 0; i < re.length; i++) {
            re[i] = l.get(l.size()-1-i);
        }
        return re;
    }
}
