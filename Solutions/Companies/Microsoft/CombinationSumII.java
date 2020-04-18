package Companies.Microsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSumII {
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> l = new ArrayList<>();
        Arrays.sort(candidates);
        helper(l, new ArrayList<>(), candidates, target, 0);
        return l;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int[] candidates, int target, int start) {
        for (int i = start; i < candidates.length; i++) {
            if (i > start && candidates[i] == candidates[i-1]) {
                continue;
            } else if (candidates[i] < target) {
                l.add(candidates[i]);
                helper(re, l, candidates, target-candidates[i], i+1);
                l.remove(l.size()-1);
            } else if (candidates[i] == target) {
                l.add(candidates[i]);
                re.add(new ArrayList<>(l));
                l.remove(l.size()-1);
            }
        }
    }
}
