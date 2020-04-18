package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class StrobogrammaticNumberI {
    public boolean isStrobogrammatic(String num) {
        Map<Character, Character> map = new HashMap<>();
        map.put('0', '0');
        map.put('1', '1');
        map.put('6', '9');
        map.put('8', '8');
        map.put('9', '6');
        for (int i = 0; i <= num.length()/2; i++) {
            if (!map.containsKey(num.charAt(i))) {
                return false;
            }
            if (!map.get(num.charAt(i)).equals(num.charAt(num.length()-i-1))) {
                return false;
            }
        }
        return true;
    }
}
