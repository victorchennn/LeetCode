package Companies.Google;

import java.util.TreeMap;

public class SnapshotArray {

    TreeMap<Integer, Integer>[] array;
    int time;

    public SnapshotArray(int length) {
        array = new TreeMap[length];
        time = 0;
        for (int i = 0; i < length; i++) {
            array[i] = new TreeMap<>();
            array[i].put(0, 0);
        }
    }

    public void set(int index, int val) {
        array[index].put(time, val);
    }

    public int snap() {
        return time++;
    }

    public int get(int index, int snap_id) {
        return array[index].floorEntry(snap_id).getValue();
    }
}
