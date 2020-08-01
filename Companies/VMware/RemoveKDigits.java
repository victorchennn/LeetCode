package Companies.VMware;

import java.util.Stack;

public class RemoveKDigits {
    public String removeKdigits(String num, int k) {
        int len = num.length();
        if (len == k) {
            return "0";
        }
        Stack<Character> s = new Stack<>();
        int i = 0;
        while (i < num.length()) {
            while (k > 0 && !s.isEmpty() && s.peek() > num.charAt(i)) {
                k--;
                s.pop();
            }
            s.push(num.charAt(i));
            i++;
        }
        while (k > 0) { // case like '1111'
            s.pop();
            k--;
        }
        StringBuilder sb = new StringBuilder();
        for (char c : s) {
            sb.append(c);
        }
        while (sb.length() > 1 && sb.charAt(0) == '0') {
            sb.deleteCharAt(0);
        }
        return sb.toString();
    }

    public String removeKdigitsII(String num, int k) {
        if (num.length() == k) {
            return "0";
        }
        if (k == 0) {
            if (num.charAt(0) == '0') {
                return removeKdigits(num.substring(1), k);
            }
            return num;
        }
        if (num.charAt(0) > num.charAt(1)) {
            return removeKdigits(num.substring(1), k-1);
        } else {
            int i = 0;
            while (i < num.length()-1 && num.charAt(i) <= num.charAt(i+1)) {
                i++;
            }
            return removeKdigits(num.substring(0, i)+num.substring(i+1), k-1);
        }
    }
}
