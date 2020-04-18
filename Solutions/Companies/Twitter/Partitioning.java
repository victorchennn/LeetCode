package Companies.Twitter;

import java.util.HashMap;
import java.util.Map;

public class Partitioning {
    static String solution(int k, int[] array) {
        if (array.length % k != 0) {
            return "No";
        }
        int limit = array.length / k;
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : array) {
            map.put(num, map.getOrDefault(num, 0)+1);
            if (map.get(num) > limit) {
                return "No";
            }
        }
        return "Yes";
    }

    public static void main(String...args) {
        System.out.println(solution(2, new int[]{1,2,3,4}));
        System.out.println(solution(3, new int[]{1,2,3,4}));
    }
}
