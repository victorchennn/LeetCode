package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class PalindromePartitioning {
    public List<List<String>> partition(String s) {
        List<List<String>> re = new ArrayList<>();
        dfs(re, new ArrayList<>(), s, 0);
        return re;
    }

    private void dfs(List<List<String>> re, List<String> l, String s, int start) {
        if (start == s.length()) {
            re.add(new ArrayList<>(l));
            return;
        }
        for (int end = start; end < s.length(); end++) {
            if (isPalindrome(s, start, end)) {
                l.add(s.substring(start, end+1));
                dfs(re, l, s, end+1);
                l.remove(l.size()-1);
            }
        }
    }

    private boolean isPalindrome(String s, int l, int r) {
        while (l < r) {
            if (s.charAt(l) != s.charAt(r)) {
                return false;
            }
            l++;
            r--;
        }
        return true;
    }
}
