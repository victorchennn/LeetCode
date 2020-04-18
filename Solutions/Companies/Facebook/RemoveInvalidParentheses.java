package Companies.Facebook;

import java.util.ArrayList;
import java.util.List;

public class RemoveInvalidParentheses {
    public List<String> removeInvalidParentheses(String s) {
        List<String> re = new ArrayList<>();
        helper(s, 0,0,new char[]{'(', ')'}, re);
        return re;
    }

    private void helper(String s, int lasti, int lastj, char[] pair, List<String> re) {
        for (int i = lasti, stack = 0; i < s.length(); i++) {
            if (s.charAt(i) == pair[0]) {
                stack++;
            }
            if (s.charAt(i) == pair[1]) {
                stack--;
            }
            if (stack >= 0) {
                continue;
            }
            for (int j = lastj; j <= i; j++) {
                if (s.charAt(j) == pair[1] && (j == lastj || s.charAt(j-1) != pair[1])) {
                    helper(s.substring(0,j)+s.substring(j+1, s.length()),i,j,pair, re);
                }
            }
            return;
        }
        String reverse = new StringBuilder(s).reverse().toString();
        if (pair[0] == '(') {
            helper(reverse, 0,0,new char[]{')', '('}, re);
        } else {
            re.add(reverse);
        }
    }
}
