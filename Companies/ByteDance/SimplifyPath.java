package Companies.ByteDance;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SimplifyPath {
    public String simplifyPath(String path) {
        Stack<String> s = new Stack<>();
        Set<String> remove = new HashSet<>(Arrays.asList(".", "..", ""));
        for (String str : path.split("/")) {
            if (str.equals("..") && s.size() > 0) {
                s.pop();
            } else if (!remove.contains(str)) {
                s.push(str);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String str : s) {
            sb.append('/' + str);
        }
        return sb.length() == 0? "/":sb.toString();
    }
}
