package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class Combinations {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> l = new ArrayList<>();
        helper(l, new ArrayList<>(), n, k, 1);
        return l;
    }

    private void helper(List<List<Integer>> l, List<Integer> cur, int n, int k, int start) {
        if (cur.size() == k) {
            l.add(new ArrayList<>(cur));
        } else {
            for (int i = start; i <= n; i++) {
                cur.add(i);
                helper(l, cur, n, k, i+1);
                cur.remove(cur.size()-1);
            }
        }
    }
}
