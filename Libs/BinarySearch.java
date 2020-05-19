package Libs;

public class BinarySearch {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length;      // r = nums.length-1
        while (l < r) {                 // l <= r
            int mid = (l + r)/2;
            if (nums[mid] == target) {
                return mid;
            }
            if (nums[mid] < target) {
                l = mid+1;
            } else {
                r = mid;                // r = mid-1
            }
        }
        return l;
    }
}
