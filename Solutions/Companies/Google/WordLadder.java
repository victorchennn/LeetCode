package Companies.Google;

import java.util.*;

public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> set = new HashSet<>(wordList);
        Queue<String> q = new LinkedList<>();
        q.add(beginWord);
        int step = 1;
        int len = beginWord.length();
        while (!q.isEmpty()) {
            int size = q.size();
            for (int i = 0; i < size; i++) {
                char[] cur = q.poll().toCharArray();
                for (int j = 0; j < len; j++) {
                    char old = cur[j];
                    for (char c = 'a'; c <= 'z'; c++) {
                        cur[j] = c;
                        String temp = new String(cur);
                        if (set.contains(temp)) {
                            if (temp.equals(endWord)) {
                                return step+1;
                            }
                            q.add(temp);
                            set.remove(temp);
                        }
                    }
                    cur[j] = old; // change back
                }
            }
            step++;
        }
        return 0;
    }
}
