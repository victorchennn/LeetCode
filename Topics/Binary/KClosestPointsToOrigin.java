package Topics.Binary;

import java.util.Arrays;

public class KClosestPointsToOrigin {
    public int[][] kClosest(int[][] points, int K) {
        int l = 0, r = points.length-1;
        while (l < r) {
            int mid = helper(points, l, r);
            if (mid == K) {
                break;
            }
            if (mid > K) {
                r = mid-1;
            } else {
                l = mid+1;
            }
        }
        return Arrays.copyOfRange(points, 0, K);
    }

    private int helper(int[][] points, int l, int r) {
        int[] p = points[l];
        while (l < r) {
            while (l < r && compare(points[r], p) >= 0) {
                r--;
            }
            points[l] = points[r];
            while (l < r && compare(points[l], p) <= 0) {
                l++;
            }
            points[r] = points[l];
        }
        points[l] = p;
        return l;
    }

    private int compare(int[] p1, int[] p2) {
        return p1[0] * p1[0] + p1[1] * p1[1] - p2[0] * p2[0] - p2[1] * p2[1];
    }

}
