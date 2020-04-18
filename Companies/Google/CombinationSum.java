package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> re = new ArrayList<>();
//        Arrays.sort(candidates);
        dfs(re, new ArrayList<>(), candidates, target,  0);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] candidates, int target, int index) {
        for (int i = index; i < candidates.length; i++) {
//            if (i > index && candidates[i] == candidates[i-1]) { // if contains duplicates
//                continue;
//            }
            if (candidates[i] < target) {
                l.add(candidates[i]);
                dfs(re, l, candidates, target-candidates[i], i+1);
                l.remove(l.size()-1);
            } else if (candidates[i] == target) {
                l.add(candidates[i]);
                re.add(new ArrayList<>(l));
                l.remove(l.size()-1);
            }
        }
    }
}
