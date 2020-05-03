package Topics.Binary;

public class SearchInsertPosition {
    public int searchInsert(int[] nums, int target) {
        int l = 0, r = nums.length;
        while (l < r) {
            int m = l + (r-l)/2;
            if (nums[m] >= target) {
                r = m;
            } else {
                l = m+1;
            }
        }
        return l;
    }

    public int searchInsertII(int[] nums, int target) {
        int l = 0, r = nums.length-1, mid;
        while (l <= r) {
            mid = l + (r-l)/2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] > target) {
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return l;
    }
}
