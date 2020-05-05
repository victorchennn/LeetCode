package Companies.Amazon;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PrisonCellsAfterNDays {
    public int[] prisonAfterNDays(int[] cells, int N) {
        Map<String, Integer> m = new HashMap<>();
        while (N > 0) {
            int[] temp = new int[8];
            m.put(Arrays.toString(cells), N--);
            for (int i = 1; i < 7; i++) {
                temp[i] = cells[i-1]==cells[i+1]?1:0;
            }
            cells = temp;
            if (m.containsKey(Arrays.toString(cells))) {
                N %= m.get(Arrays.toString(cells)) - N;
            }
        }
        return cells;
    }
}
