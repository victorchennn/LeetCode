package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DifferentWaysToAddParentheses {

    Map<String, List<Integer>> m = new HashMap<>();

    public List<Integer> diffWaysToCompute(String input) {
        List<Integer> re = new ArrayList<>();
        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c == '+' || c == '-' || c == '*') {
                String be = input.substring(0, i);
                String af = input.substring(i+1, input.length());
                for (int n1 : m.getOrDefault(be, diffWaysToCompute(be))) {
                    for (int n2 : m.getOrDefault(af, diffWaysToCompute(af))) {
                        if (c == '+') {
                            re.add(n1+n2);
                        } else if (c == '-') {
                            re.add(n1-n2);
                        } else {
                            re.add(n1*n2);
                        }
                    }
                }
            }
        }
        if (re.size() == 0) {
            re.add(Integer.valueOf(input));
        }
        m.put(input, re);
        return re;
    }
}
