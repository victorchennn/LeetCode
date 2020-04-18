package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class Permutations {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> re = new ArrayList<>();
        helper(re, new ArrayList<>(), nums);
        return re;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int[] nums) {
        if (l.size() == nums.length) {
            re.add(new ArrayList<>(l));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (!l.contains(nums[i])) {
                    l.add(nums[i]);
                    helper(re, l, nums);
                    l.remove(l.size()-1);
                }
            }
        }
    }
}
