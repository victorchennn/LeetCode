package Companies.Bloomberg;

import java.util.Arrays;

/**
 * Input: tasks = ["A","A","A","B","B","B"], n = 2
 * Output: 8
 *
 * Explanation: A -> B -> idle -> A -> B -> idle -> A -> B.
 */
public class TaskScheduler {
    public int leastInterval(char[] tasks, int n) {
        int[] count = new int[26];
        int max = 0;
        for (char t : tasks) {
            count[t-'A']++;
            max = Math.max(max, count[t-'A']);
        }
        int re = (max-1)*n + max-1;
        for (int c : count) {
            if (c == max) {
                re++;
            }
        }
        return Math.max(re, tasks.length);
    }

    public int leastIntervalII(char[] tasks, int n) {
        int[] count = new int[26];
        for (char t: tasks) {
            count[t - 'A']++;
        }
        Arrays.sort(count);
        int max = count[25] - 1, idles = max * n;
        for (int i = 24; i >= 0 && count[i] > 0; i--) {
            idles -= Math.min(count[i], max);
        }
        return idles > 0 ? idles + tasks.length : tasks.length;
    }

    public int leastIntervalIII(char[] tasks, int n) {
        int[] count = new int[26];
        for (char c : tasks) {
            count[c-'A']++;
        }
        Arrays.sort(count);
        int time = 0;
        while (count[25] > 0) {
            int i = 0;
            while (i <= n) {
                if (count[25] == 0) {
                    break;
                }
                if (i < 26 && count[25-i] > 0) {
                    count[25-i]--;
                }
                i++;
                time++;
            }
            Arrays.sort(count);
        }
        return time;
    }
}
