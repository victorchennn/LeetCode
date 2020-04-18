package Companies.Google;

public class ProductofArrayExceptSelf {
    public int[] productExceptSelf(int[] nums) {
        int[] re = new int[nums.length];
        re[0] = 1;
        for (int i = 1; i < nums.length; i++) {
            re[i] = re[i-1] * nums[i-1];
        }
        int temp = 1;
        for (int i = nums.length-2; i >= 0; i--) {
            temp *= nums[i+1];
            re[i] *= temp;
        }
        return re;
    }
}
