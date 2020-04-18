package Topics.Design;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomPickwithWeight {

    List<Integer> l;
    int limit = 0;
    Random rand = new Random();

    public RandomPickwithWeight(int[] w) {
        l = new ArrayList<>();
        for (int i : w) {
            limit += i;
            l.add(limit);
        }
    }

    public int pickIndex() {
        int target = rand.nextInt(limit);
        int le = 0, r = l.size();
        while (le < r) {
            int mid = le + (r-le)/2;
            if (target >= l.get(mid)) {
                le = mid+1;
            } else {
                r = mid;
            }
        }
        return le;
    }
}
