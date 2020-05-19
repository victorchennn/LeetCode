package Companies.Microsoft;

import java.util.List;
import java.util.Stack;

public class ExclusiveTimeofFunctions {
    public int[] exclusiveTime(int n, List<String> logs) {
        int[] res = new int[n];
        int prev = 0;
        Stack<Integer> stack = new Stack<>();
        for(String log: logs) {
            String[] parts = log.split(":");
            int id = Integer.parseInt(parts[0]);
            int time = Integer.parseInt(parts[2]);
            if(parts[1].equals("start")) {
                if(!stack.isEmpty()) {
                    res[stack.peek()] += time - prev;
                }
                prev = time;
                stack.push(id);
            } else {
                res[stack.pop()] += time - prev + 1;
                prev = time + 1;
            }
        }
        return res;
    }
}
