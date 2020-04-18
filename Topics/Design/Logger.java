package Topics.Design;

import java.util.*;

public class Logger {
//    Map<String, Integer> m = new HashMap<>();

    Set<String> s = new HashSet<>();
    Queue<Pair> q = new LinkedList<>();

    /** Initialize your data structure here. */
    public Logger() {

    }

    /** Returns true if the message should be printed in the given timestamp, otherwise returns false.
     If this method returns false, the message will not be printed.
     The timestamp is in seconds granularity. */
    public boolean shouldPrintMessage(int timestamp, String message) {
        // if (timestamp < m.getOrDefault(message, 0)) {
        //     return false;
        // }
        // m.put(message, timestamp+10);
        // return true;

        while (!q.isEmpty() && q.peek().time <= timestamp) {
            s.remove(q.poll().message);
        }
        if (!s.contains(message)) {
            s.add(message);
            q.add(new Pair(timestamp+10, message));
            return true;
        }
        return false;

    }


    private class Pair{
        String message;
        int time;
        Pair(int _time, String _message) {
            time = _time;
            message = _message;
        }
    }
}
