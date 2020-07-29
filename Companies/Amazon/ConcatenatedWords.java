package Companies.Amazon;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConcatenatedWords {
    public List<String> findAllConcatenatedWordsInADict(String[] words) {
        Set<String> set = new HashSet<>();
        List<String> re = new ArrayList<>();
        int min = Integer.MAX_VALUE;
        for (String word : words) {
            if (word.length() > 0) {
                set.add(word);
                min = Math.min(min, word.length());
            }
        }
        for (String word : words) {
            set.remove(word);
            if (dfs(set, word, min)) {
                re.add(word);
            }
            set.add(word);
        }
        return re;
    }

    private boolean dfs(Set<String> set, String word, int min) {
        if (set.contains(word)) {
            return true;
        }
        for (int i = min; i < word.length(); i++) {
            if (set.contains(word.substring(0, i)) && dfs(set, word.substring(i), min)) {
                return true;
            }
        }
        return false;
    }
}
