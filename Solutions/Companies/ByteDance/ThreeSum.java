package Companies.ByteDance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        return helper(nums, 0, 0, 3);
    }

    private List<List<Integer>> helper(int[] nums, int start, int target, int len) {
        List<List<Integer>> re = new ArrayList<>();
        if (len == 2) {
            int l = start, r = nums.length-1;
            while (l < r) {
                if (nums[l] + nums[r] == target) {
                    List<Integer> dual = new ArrayList<>();
                    dual.add(nums[l]);
                    dual.add(nums[r]);
                    re.add(dual);
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
                List<List<Integer>> next = helper(nums, i+1, target-nums[i], len-1);
                for (List<Integer> l : next) {
                    l.add(0, nums[i]);
                    re.add(l);
                }
            }
        }
        return re;
    }
}
