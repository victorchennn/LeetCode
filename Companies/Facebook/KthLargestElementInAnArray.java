package Companies.Facebook;

public class KthLargestElementInAnArray {
    public int findKthLargest(int[] nums, int k) {
        int l = 0, r = nums.length-1;
        k = nums.length-k;
        while (l < r) {
            int mid = helper(nums, l, r);
            if (mid == k) {
                break;
            }
            if (mid > k) {
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return nums[k];
    }

    private int helper(int[] nums, int l, int r) {
        int p = nums[l];
        while (l < r) {
            while (l < r && nums[r] >= p) {
                r--;
            }
            nums[l] = nums[r];
            while (l < r && nums[l] <= p) {
                l++;
            }
            nums[r] = nums[l];
        }
        nums[l] = p;
        return l;
    }
}
