package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class GenerateParentheses {
    public List<String> generateParenthesis(int n) {
        List<String> re = new ArrayList<>();
        dfs(re, "", n, n);
        return re;
    }

    private void dfs(List<String> re, String s, int l, int r) {
        if (l == 0 && r == 0) {
            re.add(s);
            return;
        }
        if (l > 0) {
            dfs(re, s+"(", l-1, r);
        }
        if (r > l) {
            dfs(re, s+")", l, r-1);
        }
    }
}
