package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinationsofPhoneNumber {
    private Map<Character, String> m = new HashMap<>();

    public List<String> letterCombinations(String digits) {
        List<String> re = new ArrayList<>();
        if (digits.length() == 0) {
            return re;
        }
        m.put('2', "abc");
        m.put('3', "def");
        m.put('4', "ghi");
        m.put('5', "jkl");
        m.put('6', "mno");
        m.put('7', "pqrs");
        m.put('8', "tuv");
        m.put('9', "wxyz");

        dfs(re, digits, 0, new StringBuilder());
        return re;
    }

    private void dfs(List<String> re, String digits, int start, StringBuilder sb) {
        if (start == digits.length()) {
            re.add(sb.toString());
            return;
        }
        for (char c : m.get(digits.charAt(start)).toCharArray()) {
            sb.append(c);
            dfs(re, digits, start+1, sb);
            sb.deleteCharAt(sb.length()-1);
        }
    }
}
