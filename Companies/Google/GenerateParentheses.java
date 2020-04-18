package Companies.Google;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> l = new ArrayList<>();
        backtrack(l, "", n, 0, 0);
        return l;
    }

    private void backtrack(List<String> l, String s, int n, int left, int right) {
        if (left == n && right == n) {
            l.add(s);
        } else {
            if (left < n) {
                backtrack(l, s + "(", n, left+1, right);
            }
            if (right < left) {
                backtrack(l, s + ")", n, left, right+1);
            }
        }

    }
}
