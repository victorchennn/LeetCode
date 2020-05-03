package Topics.BinarySearch;

public class SearchinRotatedSortedArray {
    public int search(int[] nums, int target) {
        int l = 0, r = nums.length-1;
        while (l <= r) { // <=
//            while (l < r && nums[l] == nums[r]) {   // if contains duplicates
//                l++;
//            }
            int mid = l + (r-l)/2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] >= nums[l]) {
                if (target < nums[mid] && target >= nums[l]) {
                    r = mid-1;
                } else {
                    l = mid+1;
                }
            } else {
                if (target > nums[mid] && target <= nums[r]) {
                    l = mid+1;
                } else {
                    r = mid-1;
                }
            }
        }
        return -1;
    }

    /**
     * @Follow-up: Descending order then ascending order
     */
    private static int searchII(int[] nums, int target) {
        int l = 0, r = nums.length-1;
        while (l <= r) { // <=
//            while (l < r && nums[l] == nums[r]) {   // if contains duplicates
//                l++;
//            }
            int mid = l + (r-l)/2;
            if (nums[mid] == target) {
                return mid;
            } else if (nums[mid] >= nums[l]) {
                if (target > nums[mid] && target <= nums[r]) {
                    l = mid+1;
                } else {
                    r = mid-1;
                }
            } else {
                if (target > nums[mid] && target <= nums[l]) {
                    r = mid-1;
                } else {
                    l = mid+1;
                }
            }
        }
        return -1;
    }

    public static void main(String...args) {
        System.out.println(searchII(new int[]{1,0,2,3,4,5,6,7}, 5));
        System.out.println(searchII(new int[]{1,0,2,3,4,5,6,7}, 0));
        System.out.println(searchII(new int[]{1,0,2,3,4,5,6,7}, 1));
        System.out.println(searchII(new int[]{1,0,2,3,4,5,6,7}, 7));

        System.out.println(searchII(new int[]{4,3,2,1,0,5,6,7}, 5));
        System.out.println(searchII(new int[]{4,3,2,1,0,5,6,7}, 0));
        System.out.println(searchII(new int[]{4,3,2,1,0,5,6,7}, 4));
        System.out.println(searchII(new int[]{4,3,2,1,0,5,6,7}, 7));
    }
}
