package Companies.Bloomberg;

import java.util.Stack;

public class MinimumRemovetoMakeValidParentheses {
    public String minRemoveToMakeValid(String s) {
        // Parse 1: Remove all invalid ")"
        StringBuilder sb = new StringBuilder();
        int open = 0, balance = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                open++;
                balance++;
            } else if (c == ')') {
                if (balance == 0) {
                    continue;
                }
                balance--;
            }
            sb.append(c);
        }

        // Parse 2: Remove the rightmost "("
        StringBuilder re = new StringBuilder();
        int leftOpen = open-balance;
        for (int i = 0; i < sb.length(); i++) {
            char c = sb.charAt(i);
            if (c == '(') {
                if (leftOpen == 0) {
                    continue;
                }
                leftOpen--;
            }
            re.append(c);
        }
        return re.toString();
    }

    public String minRemoveToMakeValidII(String s) {
        int openCloseCount = 0, close = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == ')') {
                close++;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (char c: s.toCharArray()) {
            if (c == '(') {
                if (openCloseCount == close) continue;
                openCloseCount++;
            } else if (c == ')') {
                close--;
                if (openCloseCount == 0) continue;
                openCloseCount--;
            }
            sb.append(c);
        }
        return sb.toString();
    }

    public String minRemoveToMakeValidIII(String s) {
        char[] ch = s.toCharArray();
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < ch.length; i++) {
            if (ch[i] == '(') {
                stack.push(i);
            } else if (ch[i] == ')') {
                if (!stack.isEmpty()) {
                    stack.pop();
                } else {
                    ch[i] = '*';
                }
            }
        }
        while (!stack.isEmpty()) {
            ch[stack.pop()] = '*';
        }
        return new String(ch).replaceAll("\\*", "");
    }
}
