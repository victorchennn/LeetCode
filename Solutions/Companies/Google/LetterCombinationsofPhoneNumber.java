package Companies.Google;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinationsofPhoneNumber {
    List<String> re = new ArrayList<>();

    public List<String> letterCombinations(String digits) {
        if (digits.length() == 0) {
            return re;
        }
        Map<String, String> m = new HashMap<>();
        m.put("2", "abc");
        m.put("3", "def");
        m.put("4", "ghi");
        m.put("5", "jkl");
        m.put("6", "mno");
        m.put("7", "pqrs");
        m.put("8", "tuv");
        m.put("9", "wxyz");
        dfs(m, "", digits);
        return re;
    }

    private void dfs(Map<String, String> m, String s, String cur) {
        if (cur.length() == 0) {
            re.add(s);
            return;
        }
        String num = m.get(cur.substring(0, 1));
        for (int i = 0; i < num.length(); i++) {
            dfs(m, s + num.substring(i, i+1), cur.substring(1));
        }
    }
}
