package Companies.Google;

public class SingleElementInASortedArray {
    public int singleNonDuplicate(int[] nums) {
        int l = 0, r = nums.length-1;
        while (l < r) {
            int mid = (l+r)/2;
            if (mid % 2 == 0) {
                if (nums[mid] == nums[mid+1]) {
                    l = mid+2;
                } else {
                    r = mid;
                }
            } else {
                if (nums[mid] == nums[mid-1]) {
                    l = mid+1;
                } else {
                    r = mid;
                }
            }
        }
        return nums[l];
    }
}
