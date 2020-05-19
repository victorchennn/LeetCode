package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TwentyFourGame {

    double ep = 0.00001;
    boolean find = false;

    public boolean judgePoint24(int[] nums) {
        List<Double> l = new ArrayList<>();
        for (int i : nums) {
            l.add((double)i);
        }
        helper(l);
        return find;
    }

    private void helper(List<Double> l) {
        if (find) {
            return;
        }
        if (l.size() == 1) {
            if (Math.abs(l.get(0)-24) < ep) {
                find = true;
                return;
            }
        }
        for (int i = 0; i < l.size(); i++) {
            for (int j = i+1; j < l.size(); j++) {
                double n = l.get(i);
                double m = l.get(j);
                List<Double> temp = new ArrayList<>(Arrays.asList(n+m, n-m, m-n, n*m));
                if (Math.abs(n) > ep) {
                    temp.add(m/n);
                }
                if (Math.abs(m) > ep) {
                    temp.add(n/m);
                }
                l.remove(j);
                l.remove(i);
                for (double add : temp) {
                    l.add(add);
                    helper(l);
                    l.remove(l.size()-1);
                }
                l.add(i, n);
                l.add(j, m);
            }
        }
    }
}
