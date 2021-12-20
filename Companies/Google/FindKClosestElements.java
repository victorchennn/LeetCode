package Companies.Google;

import java.util.ArrayList;
import java.util.List;

/**
 * return the k closest integers to x in the array.
 * The result should also be sorted in ascending order.
 *
 *  An integer a is closer to x than an integer b if:
 *  |a - x| < |b - x|, or
 *  |a - x| == |b - x| and a < b
 */
public class FindKClosestElements {
    public List<Integer> findClosestElements(int[] arr, int k, int x) {
        int l = 0, r = arr.length-k;
        while (l < r) {
            int mid = l + (r-l)/2;
            if (x - arr[mid] > arr[mid+k] - x) {
                l = mid+1;
            } else {
                r = mid;
            }
        }
        List<Integer> re = new ArrayList<>();
        for (int i = l; i < l+k; i++) {
            re.add(arr[i]);
        }
        return re;
    }
}
