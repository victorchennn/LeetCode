package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombinationSum {
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        Arrays.sort(candidates);
        List<List<Integer>> re = new ArrayList<>();
        dfs(re, new ArrayList<>(), candidates, target, 0);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] candidates, int target, int index) {
        if (target == 0) {
            re.add(new ArrayList<>(l));
            return;
        }
        for (int i = index; i < candidates.length; i++) {
//            if (i > index && candidates[i] == candidates[i-1]) {  // duplicates
//                continue;
//            }
            if (candidates[i] <= target) {
                l.add(candidates[i]);
                dfs(re, l, candidates, target-candidates[i], i);  // dfs(...., i+1)
                l.remove(l.size()-1);
            }
        }
    }

    /**
     * nums = [1, 2, 3] target = 4
     * The possible combination ways are:
     * (1, 1, 1, 1)
     * (1, 1, 2)
     * (1, 2, 1)
     * (1, 3)
     * (2, 1, 1)
     * (2, 2)
     * (3, 1)
     */
    public int combinationSum4(int[] nums, int target) {
        int[] dp = new int[target+1];
        dp[0] = 1;

        for (int sum = 1; sum <= target; sum++) {
            for (int num : nums) {
                if (sum - num >= 0) {
                    dp[sum] += dp[sum-num];
                }
            }
        }
        return dp[target];
    }
}
