package Topics.Design;

import java.util.HashMap;
import java.util.Map;

public class Marathon {
    private Sensor head, tail; // dummy
    private Map<Integer, Runner> m = new HashMap<>();

    public Marathon(int k) {
        head = new Sensor();
        tail = new Sensor();
        Sensor cur = head;
        for (int i = 0; i < k; i++) {
            Sensor newone = new Sensor();
            newone.prev = cur;
            cur.next = newone;
            newone.next = tail;
            tail.prev = newone;
            cur = cur.next;
        }
    }

    public void passMilestone(int id) {
        if (!m.containsKey(id)) {
            Runner newRunner = new Runner(id, tail.prev); // create new Runner at first sensor
            addRunner(newRunner);  // add new Runner to the last position of first sensor
            m.put(id, newRunner); // add new Runner to map
        } else {
            Runner cur = m.get(id);
            removeRunner(cur); // remove runner from old sensor
            cur.sensor = cur.sensor.prev; // set runner's new sensor be the old.prev
            addRunner(cur); // add runner to the last position of new sensor
        }
    }

    public int[] top(int k) {
        int[] re = new int[k];
        Sensor cur = head.next;
        int index = 0;
        while (index < k) {
            Runner head = cur.head;
            while (head.next != cur.tail && index < k) {
                re[index++] = head.next.id;
                head = head.next;
            }
        }
        return re;
    }

    private int getRank(int id) {
        Runner runner = m.get(id);
        Sensor cur = runner.sensor;
        int num = 0;
        while (cur.prev != head) {
            num += cur.prev.number;
            cur = cur.prev;
        }
        return num;
    }

    private void addRunner(Runner runner) {
        Sensor sensor = runner.sensor;
        sensor.number++;

        Runner before = sensor.tail.prev;
        before.next = runner;
        runner.prev = before;
        runner.next = sensor.tail;
        sensor.tail.prev = runner;

    }

    private void removeRunner(Runner runner) {
        runner.sensor.number--;

        Runner before = runner.prev;
        Runner behind = runner.next;
        before.next = behind;
        behind.prev = before;
    }


    class Sensor {
        Sensor prev, next;
        Runner head, tail; // dummy
        int number = 0;
    }

    class Runner {
        Sensor sensor;
        int id;
        Runner prev, next;
        Runner(int _id, Sensor _sensor) {
            id = _id;
            sensor = _sensor;
        }
    }
}
