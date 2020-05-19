package Companies.Microsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PermutationsII {
    public List<List<Integer>> permuteUnique(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> re = new ArrayList<>();
        helper(re, new ArrayList<>(), nums, new boolean[nums.length]);
        return re;
    }

    private void helper(List<List<Integer>> re, List<Integer> l, int[] nums, boolean[] marks) {
        if (l.size() == nums.length) {
            re.add(new ArrayList<>(l));
        } else {
            for (int i = 0; i < nums.length; i++) {
                if (marks[i] || (i > 0 && nums[i] == nums[i-1] && !marks[i-1])) { // !!!!
                    continue;
                }
                l.add(nums[i]);
                marks[i] = true;
                helper(re, l, nums, marks);
                marks[i] = false;
                l.remove(l.size()-1);
            }
        }
    }
}
