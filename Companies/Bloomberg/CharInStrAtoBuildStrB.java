package Companies.Bloomberg;

/**
 * If it's available to use char in String a to build string b?
 */
public class CharInStrAtoBuildStrB {
    public static boolean ifAva(String a, String b) {
        if (a.length() < b.length()) {
            return false;
        }
        int[] count = new int[26];
        int unique = 0;
        for (char c : b.toCharArray()) {
            if (count[c-'a'] == 0) {
                unique++;
            }
            count[c-'a']++;
        }
        for (char c : a.toCharArray()) {
            count[c-'a']--;
            if (count[c-'a'] == 0) {
                unique--;
                if (unique == 0) {
                    return true;
                }
            }
        }
        return false;
    }
}
