package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

    @Test
    void test() {
        String path1 = "/a/../../b/../c//.//";
        String path2 = "/a//b////c/d//././/..";

        assertEquals(9, path1.split("/").length);
        assertEquals(14, path2.split("/").length);
        assertEquals("/c", simplifyPath(path1));
        assertEquals("/a/b/c", simplifyPath(path2));

//        for (String str : path1.split("/")) {
//            System.out.println(str);
//        }
//        for (String str : path2.split("/")) {
//            System.out.println(str);
//        }
    }
}
