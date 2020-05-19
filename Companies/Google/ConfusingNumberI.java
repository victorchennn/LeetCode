package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class ConfusingNumberI {
    public boolean confusingNumber(int N) {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(0, 0);
        map.put(1, 1);
        map.put(6, 9);
        map.put(8, 8);
        map.put(9, 6);
        int i = 0, n = N;
        while (n > 0) {
            int r = n % 10;
            if (!map.containsKey(r)) {
                return false;
            }
            i = i*10 + map.get(r);
            n = n/10;
        }
        return i != N;
    }
}
