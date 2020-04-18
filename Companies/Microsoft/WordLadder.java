package Companies.Microsoft;

import java.util.*;

public class WordLadder {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Set<String> s = new HashSet<>(wordList);
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
                        String newWord = new String(cur);
                        if (s.contains(newWord)) {
                            if (newWord.equals(endWord)) {
                                return step+1;
                            }
                            q.add(newWord);
                            s.remove(newWord);
                        }
                    }
                    cur[j] = old;
                }
            }
            step++;
        }
        return 0;
    }
}
