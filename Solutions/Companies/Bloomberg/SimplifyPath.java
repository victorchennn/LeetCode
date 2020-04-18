package Companies.Bloomberg;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class SimplifyPath {
    public String simplifyPath(String path) {
        Stack<String> stack = new Stack<>();
        Set<String> set = new HashSet<>(Arrays.asList(".", "..", ""));

        for (String str : path.split("/")) {
            if (!stack.isEmpty() && str.equals("..")) {
                stack.pop();
            } else if (!set.contains(str)) {
                stack.push(str);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String str : stack) {
            sb.append("/" + str);
        }
        return sb.length() == 0?"/":sb.toString();
    }

    public static void main(String...args) {
        String path1 = "/a/../../b/../c//.//";
        String path2 = "/a//b////c/d//././/..";
        System.out.println(path1.split("/").length);
        System.out.println(path2.split("/").length);
        for (String str : path1.split("/")) {
            System.out.println(str);
        }
        for (String str : path2.split("/")) {
            System.out.println(str);
        }
    }
}
