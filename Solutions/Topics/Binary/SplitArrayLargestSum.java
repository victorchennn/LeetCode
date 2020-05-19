package Topics.Binary;

public class SplitArrayLargestSum {
    public int splitArray(int[] nums, int m) {
        long l = 0, r = 0;
        for (int num : nums) {
            r += num;
            if (num > l) {
                l = num;
            }
        }
        long re = r;
        while (l < r) {
            long mid = l + (r-l)/2;
            int count = 1, sum = 0;
            for (int num : nums) {
                if (sum + num > mid) {
                    sum = num;
                    count++;
                } else {
                    sum += num;
                }
            }
            if (count <= m) {
                re = Math.min(re, mid);
                r = mid;
            } else {
                l = mid+1;
            }
        }
        return (int)re;
    }
}
