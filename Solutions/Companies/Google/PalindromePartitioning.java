package Companies.Google;

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
        } else {
            for (int i = start; i < s.length(); i++) {
                if (isPalindrome(s, start, i)) {
                    l.add(s.substring(start, i+1));
                    dfs(re, l, s, i+1);
                    l.remove(l.size()-1);
                }
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
