package Libs;

public class TernarySearch {
    public static int ternarySearch(int[] nums, int key) {
        int l = 0, r = nums.length-1;
        while (l <= r) {
            int mid1 = l + (r-l)/3;
            int mid2 = r - (r-l)/3;
            if (nums[mid1] == key) {
                return mid1;
            }
            if (nums[mid2] == key) {
                return mid2;
            }
            if (key < nums[mid1]) {
                r = mid1-1;
            } else if (key > nums[mid2]) {
                l = mid2+1;
            } else {
                l = mid1+1;
                r = mid2-1;
            }
        }
        return -1;
    }

    public static void main(String...args) {
        System.out.println(ternarySearch(new int[]{1,2,3,4,5,6,7,8,9}, 5));
        System.out.println(ternarySearch(new int[]{1,2,3,4,5,6,7,8,9}, 2));
        System.out.println(ternarySearch(new int[]{1,2,3,4,5,6,7,8,9}, 9));
        System.out.println(ternarySearch(new int[]{1,2,3,4,5,6,7,8,9}, 1));
        System.out.println(ternarySearch(new int[]{1,2,3,4,5,6,7,8,9}, 0));
    }
}
