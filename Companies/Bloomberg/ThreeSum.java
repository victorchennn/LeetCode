package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        return dfs(nums, 3, 0, 0);
    }

    private List<List<Integer>> dfs(int[] nums, int k, int start, int target) {
        List<List<Integer>> re = new ArrayList<>();
        if (k == 2) {
            int l = start, r = nums.length-1;
            while (l < r) {
                if (nums[l] + nums[r] == target) {
                    re.add(new ArrayList<>(Arrays.asList(nums[l], nums[r])));
                    while (l < r && nums[l] == nums[l+1]) {
                        l++;
                    }
                    while (l < r && nums[r] == nums[r-1]) {
                        r--;
                    }
                    l++;
                    r--;
                } else if (nums[l] + nums[r] < target) {
                    l++;
                } else {
                    r--;
                }
            }
        } else {
            for (int i = start; i < nums.length; i++) {
                if (i > start && nums[i] == nums[i-1]) {
                    continue;
                }
                List<List<Integer>> afters = dfs(nums, k-1, i+1, target-nums[i]);
                for (List<Integer> after : afters) {
                    after.add(0, nums[i]);
                    re.add(after);
                }
            }
        }
        return re;
    }
}
