package Topics.Design;

import java.util.TreeMap;

public class SnapshotArray {

    TreeMap<Integer, Integer>[] array;
    int time;

    /**
     * initializes an array-like data structure with the given length.
     * Initially, each element equals 0.
     */
    public SnapshotArray(int length) {
        array = new TreeMap[length];
        time = 0;
        for (int i = 0; i < length; i++) {
            array[i] = new TreeMap<>();
            array[i].put(0, 0);
        }
    }

    /* sets the element at the given index to be equal to val. */
    public void set(int index, int val) {
        array[index].put(time, val);
    }

    /**
     * takes a snapshot of the array
     * @return the total number of times we called snap() minus 1.
     */
    public int snap() {
        return time++;
    }

    /**
     * @return the value at the given index, at the time we took the
     * snapshot with the given snap_id
     */
    public int get(int index, int snap_id) {
        return array[index].floorEntry(snap_id).getValue();
    }
}
