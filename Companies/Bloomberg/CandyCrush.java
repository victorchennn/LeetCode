package Companies.Bloomberg;

import java.util.ArrayDeque;
import java.util.Deque;

public class CandyCrush {

    public String candyCrush(String s) {
        Deque<Node> stack = new ArrayDeque<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            int count = 1;
            while (i < s.length()-1 && s.charAt(i+1) == c) {
                count++;
                i++;
            }
            if (!stack.isEmpty() && stack.peek().c == c) {
                count += stack.pop().count;
            }
            if (count < 3) {
                stack.push(new Node(c, count));
            }
        }

        StringBuilder sb = new StringBuilder();
        while (!stack.isEmpty()) {
            Node l = stack.pop();
            for (int i = l.count; i > 0; i--) {
                sb.insert(0, l.c);
            }
        }
        return sb.toString();
    }

    public static void main(String...args) {
        CandyCrush test = new CandyCrush();
//        System.out.println(test.candyCrush("acaaaacc")); // a
//        System.out.println(test.candyCrush("aaabbbc"));  // c
//        System.out.println(test.candyCrush("aabbbacd")); // cd
//        System.out.println(test.candyCrush("aabbccddeeedcba")); // ""
//        System.out.println(test.candyCrush("aaabbbacd")); // acd
        System.out.println(test.candyCrush("deeeeedbbcccccbdaa"));
    }


    private class Node {
        char c;
        int count;

        public Node(char c, int count) {
            this.c = c;
            this.count = count;
        }
    }
}
