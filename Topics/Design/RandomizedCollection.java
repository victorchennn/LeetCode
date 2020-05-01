package Topics.Design;

import java.util.*;

public class RandomizedCollection {
    private Map<Integer, Set<Integer>> map;
    private List<Integer> list;
    private Random rand;

    /**  Duplicates allowed . */
    public RandomizedCollection() {
        map = new HashMap<>();
        list = new ArrayList<>();
        rand = new Random();
    }

    /** Inserts a value to the collection. Returns true if the collection did not already contain the specified element. */
    public boolean insert(int val) {
        boolean contain = map.containsKey(val);
        map.putIfAbsent(val, new HashSet<Integer>());
        map.get(val).add(list.size());
        list.add(val);
        return !contain;
    }

    /** Removes a value from the collection. Returns true if the collection contained the specified element. */
    public boolean remove(int val) {
        if(!map.containsKey(val)) return false;
        Set<Integer> set = map.get(val);
        int valIdx = set.iterator().next();
        set.remove(valIdx);
        if(set.size() == 0) map.remove(val);
        if(valIdx != list.size() - 1) {
            int last = list.get(list.size() - 1);

            map.get(last).remove(list.size() - 1);
            map.get(last).add(valIdx);

            list.set(valIdx, last);
        }
        list.remove(list.size() - 1);
        return true;
    }

    /** Get a random element from the collection. */
    public int getRandom() {
        return list.get(rand.nextInt(list.size()));
    }
}
