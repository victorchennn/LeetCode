package Topics.Binary;

import java.util.Arrays;

public class MinimumAbsoluteDifference {
    public static int minAbsoluteDiff(int[] nums1, int[] nums2) {
        if (nums1.length > nums2.length) {
            return minAbsoluteDiff(nums2, nums1);
        }
        Arrays.sort(nums1);
        int re = Integer.MAX_VALUE, diff;
        for (int num : nums2) {
            int index = find(nums1, num);
            if (index == 0) {
                diff = Math.abs(nums1[index] - num);
            } else if (index == nums1.length) {
                diff = Math.abs(nums1[index-1] - num);
            } else {
                diff = Math.min(Math.abs(nums1[index] - num), Math.abs(nums1[index-1] - num));
            }
            re = Math.min(re, diff);
        }
        return re;
    }

    private static int find(int[] nums, int num) {
        int l = 0, r = nums.length;
        while (l < r) {
            int mid = l + (r-l)/2;
            if (nums[mid] == num) {
                return mid;
            } else if (nums[mid] < num) {
                l = mid+1;
            } else {
                r = mid;
            }
        }
        return l;
    }

    public static void main(String...args) {
        System.out.println(minAbsoluteDiff(new int[]{5,6,7,3}, new int[]{1,2,4,9}));
    }
}
