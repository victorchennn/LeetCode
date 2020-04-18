package Companies.Microsoft;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetterCombinationsofPhoneNumber {
    public List<String> letterCombinations(String digits) {
        List<String> re = new ArrayList<>();
        if (digits.length() == 0) {
            return re;
        }
        Map<Character, String> m = new HashMap<>();
        m.put('2', "abc");
        m.put('3', "def");
        m.put('4', "ghi");
        m.put('5', "jkl");
        m.put('6', "mno");
        m.put('7', "pqrs");
        m.put('8', "tuv");
        m.put('9', "wxyz");
        dfs(re, m, digits, new StringBuilder());
        return re;
    }

    private void dfs(List<String> re, Map<Character, String> m, String digits, StringBuilder sb) {
        if (digits.length() == 0) {
            re.add(sb.toString());
            return;
        }
        for (char c : m.get(digits.charAt(0)).toCharArray()) {
            sb.append(c);
            dfs(re, m, digits.substring(1), sb);
            sb.deleteCharAt(sb.length()-1);
        }
    }
}
