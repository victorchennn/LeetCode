package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StrobogrammaticNumberII {
    public List<String> findStrobogrammatic(int n) {
        return helper(n, n);
    }

    private List<String> helper(int n, int m) {
        List<String> re = new ArrayList<>();
        if (n == 0) {
            return new ArrayList<>(Arrays.asList(""));
        }
        if (n == 1) {
            return new ArrayList<>(Arrays.asList("0", "1", "8"));
        }
        for (String s : helper(n-2, m)) {
            if (n != m) {
                re.add("0" + s + "0");
            }
            re.add("1" + s + "1");
            re.add("6" + s + "9");
            re.add("8" + s + "8");
            re.add("9" + s + "6");
        }
        return re;
    }
}
