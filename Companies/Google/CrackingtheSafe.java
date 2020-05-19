package Companies.Google;

import java.util.HashSet;
import java.util.Set;

public class CrackingtheSafe {
    public String crackSafe(int n, int k) {
        StringBuilder re = new StringBuilder();
        for (int i = 0; i < n; i++) {
            re.append('0');
        }
        Set<String> s = new HashSet<>();
        s.add(re.toString());
        int total = (int) Math.pow(k, n);
        dfs(re, s, total, n, k);
        return re.toString();
    }

    private boolean dfs(StringBuilder re, Set<String> s, int total, int n, int k) {
        if (s.size() == total) {
            return true;
        }
        String prev = re.substring(re.length() - n + 1);
        for (int i = 0; i < k; i++) {
            String cur = prev + i;
            if (!s.contains(cur)) {
                s.add(cur);
                re.append(i);
                if (dfs(re, s, total, n, k)) {
                    return true;
                } else {
                    s.remove(cur);
                    re.deleteCharAt(re.length()-1);
                }
            }
        }
        return false;
    }
}
