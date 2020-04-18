package Companies.Bloomberg;

public class TrappingRainWater {
    public int trap(int[] height) {
        int l = 0, r = height.length-1, lmax = 0, rmax = 0, sum = 0;
        while (l < r) {
            if (height[l] < height[r]) {
                if (height[l] >= lmax) {
                    lmax = height[l];
                } else {
                    sum += lmax-height[l];
                }
                l++;
            } else {
                if (height[r] >= rmax) {
                    rmax = height[r];
                } else {
                    sum += rmax-height[r];
                }
                r--;
            }
        }
        return sum;
    }
}
