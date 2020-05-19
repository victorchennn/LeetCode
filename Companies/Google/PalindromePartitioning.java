package Companies.Google;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

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

    /* minimum cuts needed for a palindrome partitioning of s. */
    public int minCut(String s) {
        if (s.length() <= 1) {
            return 0;
        }
        int[] cut = IntStream.range(0, s.length()).toArray(); // for cut[], 1char need 0cut, 2char need 1cut, 3char need 2cut....
        for (int mid = 1; mid < s.length(); mid++) {
            for (int l = mid, r = mid; l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r); l--, r++) {
                int newcut = (l == 0?0:cut[l-1]+1);
                cut[r] = Math.min(cut[r], newcut);
            }
            for (int l = mid-1, r = mid; l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r); l--, r++) {
                int newcut = (l == 0?0:cut[l-1]+1);
                cut[r] = Math.min(cut[r], newcut);
            }
        }
        return cut[s.length()-1];
    }
}
