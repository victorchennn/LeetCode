package Companies.Microsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Game24 {
    private boolean find = false;
    private double EP = 0.00001;

    public boolean judgePoint24(int[] nums) {
        List<Double> l = new ArrayList<>();
        for (int num : nums) {
            l.add((double)num);
        }
        helper(l);
        return find;
    }

    private void helper(List<Double> l) {
        if (find) {
            return;
        }
        if (l.size() == 1) {
            if (Math.abs(l.get(0) - 24) < EP) {
                find = true;
                return;
            }
        }
        for (int i = 0; i < l.size(); i++) {
            for (int j = i+1; j < l.size(); j++) {
                double n = l.get(i);
                double m = l.get(j);
                List<Double> temp = new ArrayList<>(Arrays.asList(n+m, n-m, m-n, n*m));
                if (Math.abs(n) > EP) {
                    temp.add(m/n);
                }
                if (Math.abs(m) > EP) {
                    temp.add(n/m);
                }
                l.remove(j); // j has to move at first, then i
                l.remove(i);
                for (double next : temp) {
                    l.add(next);
                    helper(l);
                    l.remove(l.size()-1);
                }
                l.add(i, n); // add in the opposite way
                l.add(j, m);
            }
        }
    }
}
