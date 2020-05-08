package Companies.GoldmanSachs;

import java.util.ArrayList;
import java.util.List;

/**
 * @see Companies.Bloomberg.CombinationSum
 * @see Permutations
 */
public class Subsets {
    public List<List<Integer>> subsetsWithDup(int[] nums) {
//        Arrays.sort(nums);
        List<List<Integer>> re = new ArrayList<>();
        dfs(re, new ArrayList<>(), nums, 0);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] nums, int start) {
        re.add(new ArrayList<>(l));
        for (int i = start; i < nums.length; i++) {
//            if (i > start && nums[i] == nums[i-1]) {  // duplicates
//                continue;
//            }
            l.add(nums[i]);
            dfs(re, l, nums, i+1);
            l.remove(l.size()-1);
        }
    }
}
