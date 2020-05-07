package Companies.Microsoft;

public class FindSingleElementinSortedArray {

    public int singleNonDuplicate(int[] nums) {
        int ans = 0;
        for (int num : nums) {
            ans = ans ^ num;
        }
        return ans;
    }

    public int singleNonDuplicateII(int[] nums) {
        int l = 0, r = nums.length-1;
        while (l < r) {
            int m = l + (r-l)/2;
            if (m % 2 == 1) {
                m--;
            }
            if (nums[m] == nums[m+1]) {
                l = m+2;
            } else {
                r = m;
            }
        }
        return nums[l];
    }
}
