package Topics.Design;

import java.util.*;

public class RandomizedSet {
    private List<Integer> l = new ArrayList<>();
    private Map<Integer, Integer> m = new HashMap<>(); // value, position
    private Random rand = new Random();

    /** Insert-Delete-GetRandom O(1). */
    public RandomizedSet() {

    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if (m.containsKey(val)) {
            return false;
        }
        m.put(val, l.size());
        l.add(val);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if (!m.containsKey(val)) {
            return false;
        }
        int index = m.get(val);
        m.remove(val);
        if (index != l.size()-1) {
            int last = l.get(l.size()-1);
            m.put(last, index); // no need to swap
            l.set(index, last);
        }
        l.remove(l.size()-1);
        return true;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        return l.get(rand.nextInt(l.size()));
    }
}
