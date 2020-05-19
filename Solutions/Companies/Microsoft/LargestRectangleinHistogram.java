package Companies.Microsoft;

public class LargestRectangleinHistogram {
    public int largestRectangleArea(int[] heights) {
        int len = heights.length;
        if (len == 0) {
            return 0;
        }
        int[] l = new int[len];
        int[] r = new int[len];
        l[0] = -1;
        for (int i = 1; i < len; i++) {
            int p = i-1;
            while (p >= 0 && heights[p] >= heights[i]) {
                p = l[p];
            }
            l[i] = p;
        }
        r[len-1] = len;
        for (int i = len-2; i >= 0; i--) {
            int p = i+1;
            while (p < len && heights[p] >= heights[i]) {
                p = r[p];
            }
            r[i] = p;
        }
        int max = 0;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, heights[i]*(r[i]-l[i]-1));
        }
        return max;
    }
}
