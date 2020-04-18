package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> re = new ArrayList<>();
        dfs(re, new ArrayList<>(), nums);
//        Arrays.sort(nums);
//        dfs(re, new ArrayList<>(), nums, new boolean[nums.length]);
        return re;
    }

    private void dfs(List<List<Integer>> re, List<Integer> l, int[] nums) {
        if (l.size() == nums.length) {
            re.add(new ArrayList<>(l));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (!l.contains(nums[i])) {
                    l.add(nums[i]);
                    dfs(re, l, nums);
                    l.remove(l.size()-1);
                }
            }
        }
    }

    /** Contain duplicates. **/
    private void dfs(List<List<Integer>> re, List<Integer> l, int[] nums, boolean[] marks) {
        if (l.size() == nums.length) {
            re.add(new ArrayList<>(l));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (marks[i] || (i > 0 && nums[i] == nums[i-1] && !marks[i-1])) {
                    continue;
                }
                l.add(nums[i]);
                marks[i] = true;
                dfs(re, l, nums, marks);
                marks[i] = false;
                l.remove(l.size()-1);
            }
        }
    }
}
