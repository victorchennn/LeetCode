package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class UniqueWordAbbreviation {

    Map<String, String> m;

    public UniqueWordAbbreviation(String[] dictionary) {
        m = new HashMap<>();
        for (String s : dictionary) {
            String key = abbr(s);
            if (m.containsKey(key)) {
                if (!m.get(key).equals(s)) {
                    m.put(key, "");
                }
            } else {
                m.put(key, s);
            }
        }
    }

    public boolean isUnique(String word) {
        return !m.containsKey(abbr(word)) || m.get(abbr(word)).equals(word);
    }

    private String abbr(String s) {
        if (s.length() <= 2) {
            return s;
        }
        return s.charAt(0) + Integer.toString((s.length()-2)) + s.charAt(s.length()-1);
    }
}
