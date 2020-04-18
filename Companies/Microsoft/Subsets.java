package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class Subsets {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> re = new ArrayList<>();
        helper(re, new ArrayList<>(), nums, 0);
        return re;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int[] nums, int start) {
        re.add(new ArrayList<>(l));
        for (int i = start; i < nums.length; i++) {
            l.add(nums[i]);
            helper(re, l, nums, i+1);
            l.remove(l.size()-1);
        }
    }
}
