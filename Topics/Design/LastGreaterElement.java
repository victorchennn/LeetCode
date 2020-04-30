package Topics.Design;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class LastGreaterElement {
    private TreeMap<Integer, Integer> m;
    private List<Integer> l;
    private int index = 0;

    public LastGreaterElement() {
        m = new TreeMap<>();
        l = new ArrayList<>();
    }

    public void append(int value) {
        m.put(value, -1);
        l.add(value);
        for (int key : m.keySet()) {
            if (key < value) {
                m.put(key, index);
            } else {
                break;
            }
        }
        index++;
    }

    public int get(int index) {
        return m.get(l.get(index));
    }
}
