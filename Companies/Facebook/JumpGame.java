package Companies.Facebook;

public class JumpGame {
    public boolean canJump(int[] nums) {
        int last = nums.length-1;
        for (int i = nums.length-1; i >= 0; i--) {
            if (i + nums[i] >= last) {
                last = i;
            }
        }
        return last == 0;
    }

    /* minimum number of jumps */
    public int jump(int[] nums) {
        int re = 0, end = 0, fast = 0;
        for (int i = 0; i < nums.length-1; i++) { // i < nums.length-1 !!
            fast = Math.max(fast, i + nums[i]);
            if (i == end) {
                re++;
                end = fast;
            }
        }
        return re;
    }

    public boolean canReach(int[] arr, int start) {
        if (start >= 0 && start < arr.length && arr[start] < arr.length) {
            int jump = arr[start];
            arr[start] += arr.length; // make it impossible to reach
            return jump == 0 || canReach(arr, start+jump) || canReach(arr, start-jump);
        }
        return false;
    }
}
