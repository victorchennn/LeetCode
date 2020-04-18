package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> re = new ArrayList<>();
        helper(re, new ArrayList<>(), candidates, target, 0, 0);
        return re;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int[] candidates, int target, int start, int sum) {
        for (int i = start; i < candidates.length; i++) {
            if (candidates[i] + sum == target) {
                l.add(candidates[i]);
                re.add(new ArrayList<>(l));
                l.remove(l.size()-1);
            } else if (candidates[i] + sum < target) {
                l.add(candidates[i]);
                helper(re, l, candidates, target, i, candidates[i] + sum);
                l.remove(l.size()-1);
            }
        }
    }
}
