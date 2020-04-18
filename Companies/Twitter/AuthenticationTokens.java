package Companies.Twitter;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationTokens {
    public int solution(int n, int[][] commands) {
        Map<Integer, Integer> m = new HashMap<>();
        int time = 0;
        for (int[] command : commands) {
            int action = command[0];
            int id = command[1];
            time = command[2];
            if (action == 0) {
                m.put(id, time+n);
            } else {
                if (m.containsKey(id)) {
                    int exp = m.get(id);
                    if (exp >= time) {
                        m.put(id, time+n);
                    }
                }
            }
        }
        int re = 0;
        for (int i : m.values()) {
            if (i >= time) {
                re++;
            }
        }
        return re;
    }

    public static void main(String...args) {
        AuthenticationTokens test = new AuthenticationTokens();
        System.out.println(test.solution(4, new int[][]{{0,1,1},{0,2,2},{1,1,5},{1,2,7}}));
    }
}
