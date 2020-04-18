package Companies.Google;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OptimizeWaterDistributionInAVillage {

    int[] unf;

    public int minCostToSupplyWater(int n, int[] wells, int[][] pipes) {
        unf = new int[n+1];
        List<int[]> l = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            unf[i+1] = i+1;
            l.add(new int[]{0, i+1, wells[i]});
        }
        for (int[] pipe : pipes) {
            l.add(pipe);
        }
        Collections.sort(l, (a, b)->a[2]-b[2]);
        int re = 0;
        for (int[] comb : l) {
            int x = find(comb[0]), y = find(comb[1]);
            if (x != y) {
                re += comb[2];
                unf[x] = y;
            }
        }
        return re;
    }

    private int find(int x) {
        if (unf[x] != x) {
            unf[x] = find(unf[x]);
        }
        return unf[x];
    }
}
