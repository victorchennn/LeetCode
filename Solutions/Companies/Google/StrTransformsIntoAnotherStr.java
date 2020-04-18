package Companies.Google;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StrTransformsIntoAnotherStr {
    public boolean canConvert(String str1, String str2) {
        if (str1.equals(str2)) {
            return true;
        }
        Map<Character, Character> m = new HashMap<>();
        for (int i = 0; i < str1.length(); i++) {
            if (m.getOrDefault(str1.charAt(i), str2.charAt(i)) != str2.charAt(i)) {
                return false;
            }
            m.put(str1.charAt(i), str2.charAt(i));
        }
        return new HashSet<Character>(m.values()).size() < 26;
    }
}
