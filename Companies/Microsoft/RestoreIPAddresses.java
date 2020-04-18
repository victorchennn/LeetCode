package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class RestoreIPAddresses {
    public List<String> restoreIpAddresses(String s) {
        List<String> re = new ArrayList<>();
        dfs(re, s, "", 0);
        return re;
    }

    private void dfs(List<String> re, String s, String path, int k) {
        if (s.length() == 0 || k == 4) {
            if (s.length() == 0 && k == 4) {
                re.add(path.substring(1));
            }
            return;
        }
        for (int i = 1; i <= (s.charAt(0) == '0'? 1:3) && i <= s.length(); i++) {
            String num = s.substring(0, i);
            if (Integer.valueOf(num) <= 255) {
                dfs(re, s.substring(i), path + "." + num, k+1);
            }
        }
    }
}
