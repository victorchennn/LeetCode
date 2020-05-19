package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class LexicographicalNumbers {
    public List<Integer> lexicalOrder(int n) {
        List<Integer> re = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            dfs(re, i, n);
        }
        return re;
    }

    private void dfs(List<Integer> re, int num, int n) {
        if (num > n) {
            return;
        }
        re.add(num);
        for (int i = 0; i < 10; i++) {
            dfs(re, num*10+i, n);
        }
    }
}
