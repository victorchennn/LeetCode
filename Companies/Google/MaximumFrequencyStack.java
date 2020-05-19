package Companies.Google;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class MaximumFrequencyStack {
    private Map<Integer, Integer> freq;
    private Map<Integer, Stack<Integer>> ties;
    private int max;

    public MaximumFrequencyStack() {
        max = 0;
        freq = new HashMap<>();
        ties = new HashMap<>();
    }

    public void push(int x) {
        int i = freq.getOrDefault(x, 0)+1;
        freq.put(x, i);
        if (i > max) {
            max = i;
        }
        ties.computeIfAbsent(i, k->new Stack<>()).push(x);
    }

    public int pop() {
        int re = ties.get(max).pop();
        freq.put(re, freq.get(re)-1);
        if (ties.get(max).size() == 0) {
            max--;
        }
        return re;
    }
}
