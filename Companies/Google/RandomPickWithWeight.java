package Companies.Google;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPickWithWeight {

    List<Integer> l;
    int limit;
    Random rand;

    public RandomPickWithWeight(int[] w) {
        limit = 0;
        rand = new Random();
        l = new ArrayList<>();
        for (int i : w) {
            limit += i;
            l.add(limit);
        }
    }

    public int pickIndex() {
        int target = rand.nextInt(limit);
        int low = 0, r = l.size();
        while (low < r) {
            int mid = (low+r)/2;
            if (target >= l.get(mid)) {
                low = mid+1;
            } else {
                r = mid;
            }
        }
        return low;
    }
}
