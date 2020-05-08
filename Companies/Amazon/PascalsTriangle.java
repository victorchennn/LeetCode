package Companies.Amazon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @see Triangle
 */
public class PascalsTriangle {
    /* Iteration */
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

    /* Recursion */
    public List<List<Integer>> generateII(int numRows) {
        List<List<Integer>> re = new ArrayList<>();
        if (numRows == 0) {
            return re;
        }
        helper(re, numRows);
        return re;
    }

    private void helper(List<List<Integer>> re, int numRows) {
        if (numRows == 1) {
            re.add(Arrays.asList(1));
        } else {
            helper(re, numRows-1);
            List<Integer> prev = re.get(numRows-2);
            List<Integer> cur = new ArrayList<>();
            for (int i = 0; i < prev.size(); i++) {
                if (i == 0) {
                    cur.add(1);
                } else {
                    cur.add(prev.get(i) + prev.get(i-1));
                }
                if (i == prev.size()-1) {
                    cur.add(1);
                }
            }
            re.add(cur);
        }
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
