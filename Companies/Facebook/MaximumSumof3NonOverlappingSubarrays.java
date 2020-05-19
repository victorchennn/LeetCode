package Companies.Facebook;

public class MaximumSumof3NonOverlappingSubarrays {
    public int[] maxSumOfThreeSubarrays(int[] nums, int k) {
        int[] wins = new int[nums.length-k+1];
        int curSum = 0;
        for (int i = 0; i < nums.length; i++) {
            curSum += nums[i];
            if (i >= k) {
                curSum -= nums[i-k];
            }
            if (i >= k-1) {
                wins[i-k+1] = curSum;
            }
        }

        int[] lMax = new int[wins.length];
        int[] rMax = new int[wins.length];
        int p = 0;
        for (int i = 0; i < wins.length; i++) {
            if (wins[i] > wins[p]) {
                p = i;
            }
            lMax[i] = p;
        }
        p = wins.length-1;
        for (int i = wins.length-1; i >= 0; i--) {
            if (wins[i] >= wins[p]) {
                p = i;
            }
            rMax[i] = p;
        }

        int[] re = new int[]{-1, -1, -1};
        for (int m = k; m < wins.length-k; m++) {
            int l = lMax[m-k], r = rMax[m+k];
            if (re[0] == -1 || wins[l]+wins[r]+wins[m] > wins[re[0]]+wins[re[1]]+wins[re[2]]) {
                re[0] = l;
                re[1] = m;
                re[2] = r;
            }
        }
        return re;
    }
}
