package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class CombinationSumIII {
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> re = new ArrayList<>();
        helper(re, new ArrayList<>(), k, n, 1);
        return re;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int k, int n, int start) {
        if (k == 0 && n == 0) {
            re.add(new ArrayList<>(l));
        } else {
            for (int i = start; i <= 9; i++) {
                if (!l.contains(i) && i <= n && k > 0) {
                    l.add(i);
                    helper(re, l, k-1, n-i, i+1);
                    l.remove(l.size()-1);
                }
            }
        }
    }
}
