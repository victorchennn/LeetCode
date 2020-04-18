package Companies.ByteDance;

public class ContainerWithMostWater {
    public int maxArea(int[] height) {
        int re = 0, l = 0, r = height.length-1;
        while (l < r) {
            re = Math.max(re, (r-l)*Math.min(height[l], height[r]));
            if (height[l] < height[r]) {
                l++;
            } else {
                r--;
            }
        }
        return re;
    }
}
