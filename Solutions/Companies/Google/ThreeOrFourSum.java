package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeOrFourSum {
    public List<List<Integer>> fourSum(int[] nums, int target) {
        Arrays.sort(nums);
        return helper(nums, target, 0, 4);
    }

    private List<List<Integer>> helper(int[] nums, int target, int start, int len) {
        List<List<Integer>> re = new ArrayList<>();
        if (len == 2) {
            int l = start, r = nums.length-1; // careful!
            while (l < r) {
                if (nums[l] + nums[r] == target) {
                    List<Integer> temp = new ArrayList<>(Arrays.asList(nums[l], nums[r]));
                    re.add(temp);
                    while (l < r && nums[l] == nums[l+1]) { // careful!
                        l++;
                    }
                    while (l < r && nums[r] == nums[r-1]) { // careful!
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
                if (i > start && nums[i] == nums[i-1]) {    // careful!
                    continue;
                }
                List<List<Integer>> after = helper(nums, target - nums[i], i+1, len-1);
                for (List<Integer> l : after) {
                    l.add(0, nums[i]);
                    re.add(l);
                }
            }
        }
        return re;
    }
}
