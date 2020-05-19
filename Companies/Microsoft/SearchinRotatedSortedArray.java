package Companies.Microsoft;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void test() {
        assertEquals(5, searchII(new int[]{1,0,2,3,4,5,6,7}, 5));
        assertEquals(1, searchII(new int[]{1,0,2,3,4,5,6,7}, 0));
        assertEquals(0, searchII(new int[]{1,0,2,3,4,5,6,7}, 1));
        assertEquals(7, searchII(new int[]{1,0,2,3,4,5,6,7}, 7));

        assertEquals(5, searchII(new int[]{4,3,2,1,0,5,6,7}, 5));
        assertEquals(4, searchII(new int[]{4,3,2,1,0,5,6,7}, 0));
        assertEquals(0, searchII(new int[]{4,3,2,1,0,5,6,7}, 4));
        assertEquals(7, searchII(new int[]{4,3,2,1,0,5,6,7}, 7));

    }
}