package Companies.Google;

public class CapacityToShipPackagesWithinDDays {
    public int shipWithinDays(int[] weights, int D) {
        int l = 0, r = 0;
        for (int w : weights) {
            r += w;
            l = Math.max(l, w);
        }
        while (l < r) {
            int mid = l + (r-l)/2;
            int sum = 0, day = 1;
            for (int w : weights) {
                if (sum + w > mid) {
                    day++;
                    sum = 0;
                }
                sum += w;
            }
            if (day > D) {
                l = mid+1;
            } else {
                r = mid;
            }
        }
        return l;
    }
}
