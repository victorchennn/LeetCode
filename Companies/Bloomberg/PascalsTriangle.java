package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.List;

public class PascalsTriangle {
    public List<List<Integer>> generate(int numRows) {
        List<List<Integer>> re = new ArrayList<>();
        List<Integer> l = new ArrayList<>();
        for (int i = 0; i < numRows; i++) {
            l.add(0, 1);
            for (int j = 1; j < l.size()-1; j++) {
                l.set(j, l.get(j)+l.get(j+1));
            }
            re.add(new ArrayList<>(l));
        }
        return re;
    }

    /* k-th index row of the Pascal's triangle. */
    public List<Integer> getRow(int rowIndex) {
        List<Integer> re = new ArrayList<>();
        for (int i = 0; i <= rowIndex; i++) {
            re.add(0, 1);
            for (int j = 1; j < re.size()-1; j++) {
                re.set(j, re.get(j) + re.get(j+1));
            }
        }
        return re;
    }
}
