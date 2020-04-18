package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> l = new ArrayList<>();
        helper(l, "", n, n);
        return l;
    }

    private void helper(List<String> list, String s, int l, int r) {
        if (l == 0 && r == 0) {
            list.add(s);
        } else {
            if (l > 0) {
                helper(list, s + '(', l-1, r);
            }
            if (l < r) {
                helper(list, s + ')', l, r-1);
            }
        }
    }
}
