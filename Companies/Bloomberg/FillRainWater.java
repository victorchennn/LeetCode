package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Fill water to the matrix, always start from the shallowest one
 * Exp:
 *
 * volume = 2
 * {{2,2,2}   {{2,2,2}   {{   2,   2,   2}
 *  {1,2,0} -> {1,2,1} -> {1.25,   2,1.25}
 *  {1,1,2}}   {1,1,2}}   {1.25,1.25,   2}}
 */
public class FillRainWater {
    public double[][] fill(double[][] matrix, double volume) {
        TreeMap<Double, List<Integer>> tm = new TreeMap<>();
        int m = matrix.length, n = matrix[0].length;
        for (int i = 0; i < m*n; i++) {
            int r = i/n, c = i%n;
            if (!tm.containsKey(matrix[r][c])) {
                tm.put(matrix[r][c], new LinkedList<>());
            }
            tm.get(matrix[r][c]).add(i);
        }
        while (volume > 0) {
            double lowest = tm.firstKey();
            List<Integer> pos = tm.remove(lowest);
            int count = pos.size();
            double add;
            if (count < m*n) {
                add = Math.min(tm.firstKey()-lowest, volume/count);
            } else {
                add = volume/count;
            }
            for (int p : pos) {
                int r = p/n, c = p%n;
                matrix[r][c] = lowest+add;
                volume -= add;
                if (!tm.containsKey(matrix[r][c])) {
                    tm.put(matrix[r][c], new LinkedList<>());
                }
                tm.get(matrix[r][c]).add(p);
            }
        }
        return matrix;
    }

    @Test
    void test() {
        assertEquals(true, Arrays.deepEquals(new double[][]{{2,2,2},{1.25,2,1.25},{1.25,1.25,2}},
                fill(new double[][]{{2,2,2},{1,2,0},{1,1,2}}, 2)));
    }
}
