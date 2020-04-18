package Companies.Google;

import java.util.*;

public class InsertDeleteGetRandomO1 {

    Map<Integer, Integer> m;
    List<Integer> l;
    Random rand;


    /** Initialize your data structure here. */
    public InsertDeleteGetRandomO1() {
        rand = new Random();
        m = new HashMap<>();
        l = new ArrayList<>();
    }

    /** Inserts Companies.Amazon value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if (m.containsKey(val)) {
            return false;
        }
        m.put(val, l.size());
        l.add(val);
        return true;
    }

    /** Removes Companies.Amazon value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if (!m.containsKey(val)) {
            return false;
        }
        swap(m.get(val), l.size()-1);
        m.remove(val);
        l.remove(l.size()-1);
        return true;
    }

    private void swap(int i, int j) {
        if (i != j) {                       // check
            int temp = l.get(j);
            l.set(j, l.get(i));
            l.set(i, temp);
            m.put(l.get(i), i);
        }
    }

    /** Get Companies.Amazon random element from the set. */
    public int getRandom() {
        return l.get(rand.nextInt(l.size()));
    }
}
