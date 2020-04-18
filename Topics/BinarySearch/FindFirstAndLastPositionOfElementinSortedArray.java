package Topics.BinarySearch;

public class FindFirstAndLastPositionOfElementinSortedArray {
    public int[] searchRange(int[] nums, int target) {
        int[] re = {-1,-1};
        int l = binary(nums, target, true);
        if (l == nums.length || nums[l] != target) {
            return re;
        }
        re[0] = l;
        re[1] = binary(nums, target, false)-1;
        return re;
    }

    private int binary(int[] nums, int target, boolean left) {
        int l = 0, r = nums.length;
        while (l < r) {
            int mid = l + (r-l)/2;
            if (nums[mid] > target || nums[mid] == target && left) {
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return l;
    }
}
