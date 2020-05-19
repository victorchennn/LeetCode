package Companies.Bloomberg;

/**
 * A string to be valid if all characters of the string appear the same number of times.
 * It's also valid if can remove just 1 character, and the remaining characters will occur the same number of times.
 */
public class SherlockandtheValidString {
    public String isValid(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        int max = -1;
        boolean exist = false;
        for (int i = 0; i < 26; i++) {
            if (count[i] == 0) {
                continue;
            } else if (max == -1) {
                max = count[i];
            } else if (count[i] == max) {
                continue;
            } else if (!exist && (count[i] == max+1 || count[i] == 1)) {
                exist = true;
            } else {
                return "NO";
            }

        }
        return "YES";
    }
}
