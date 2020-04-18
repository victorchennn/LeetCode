package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> re = new ArrayList<>();
//        Arrays.sort(nums); // If contains duplicates
        dfs(re, new ArrayList<>(), nums, 0);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] nums, int start) {
        re.add(new ArrayList<>(l));
        for (int i = start; i < nums.length; i++) {
            l.add(nums[i]);
            dfs(re, l, nums, i+1);
            l.remove(l.size()-1);
//            l.remove(Integer.valueOf(nums[i]));
        }
    }

    /** Contain duplicates **/
    private void dfs_II(List<List<Integer>> re, List<Integer> l, int[] nums, int start) {
        re.add(new ArrayList<>(l));
        for (int i = start; i < nums.length; i++) {
            if (i > start && nums[i] == nums[i-1]) { // Check
                continue;
            }
            l.add(nums[i]);
            dfs_II(re, l, nums, i+1);
            l.remove(l.size()-1);
        }
    }
}
