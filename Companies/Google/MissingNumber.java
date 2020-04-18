package Companies.Google;

public class MissingNumber {
    public int missingNumber(int[] nums) {
        int re = nums.length;
        for (int i = 0; i < nums.length; i++) {
            re ^= i ^ nums[i];
        }
        return re;
    }
}
