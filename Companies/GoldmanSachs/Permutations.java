package Companies.GoldmanSachs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see Companies.Bloomberg.CombinationSum
 * @see Subsets
 */
public class Permutations {
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> re = new ArrayList<>();
        dfs(re, new ArrayList<>(), nums, new boolean[nums.length]);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] nums, boolean[] used) {
        if (l.size() == nums.length) {
            re.add(new ArrayList<>(l));
            return;
        }
        for (int i = 0; i < nums.length; i++) {
            if (used[i] || i > 0 && nums[i] == nums[i-1] && !used[i-1]) { // contain duplicates
                continue;
            }
            used[i] = true;
            l.add(nums[i]);
            dfs(re, l, nums, used);
            l.remove(l.size()-1);
            used[i] = false;
        }
    }
}
