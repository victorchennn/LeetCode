package Topics.BTree;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BTPathInZigzagLabelled {
    public List<Integer> pathInZigZagTree(int label) {
        List<Integer> re = new ArrayList<>();
        int count = 1, level = 1;
        while (label >= count*2) {
            count *= 2;
            level++;
        }
        while (label != 0) {
            re.add(label);
            int max = (int)Math.pow(2, level)-1;
            int min = (int)Math.pow(2, level-1);
            label = (max+min-label)/2;
            level--;
        }
        Collections.reverse(re);
        return re;
    }
}
