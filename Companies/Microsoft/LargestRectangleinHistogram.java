package Companies.Microsoft;

public class LargestRectangleinHistogram {
    public int largestRectangleArea(int[] heights) {
        if (heights.length == 0) {
            return 0;
        }
        int len = heights.length;
        int[] left = new int[len];
        int[] right = new int[len];
        left[0] = -1;
        right[len-1] = len;

        for (int i = 1; i < len; i++) {
            int p = i-1;
            while (p >= 0 && heights[p] >= heights[i]) {
                p = left[p];
            }
            left[i] = p;
        }

        for (int i = len-2; i >=0; i--) {
            int p = i+1;
            while (p < len && heights[p] >= heights[i]) {
                p = right[p];
            }
            right[i] = p;
        }

        int max = 0;
        for (int i = 0; i < len; i++) {
            max = Math.max(max, heights[i]*(right[i]-left[i]-1));
        }
        return max;
    }
}
