package Companies.Google;

public class ExpressiveWords {
    public int expressiveWords(String S, String[] words) {
        int re = 0;
        for (String word : words) {
            if (check(word, S)) {
                re++;
            }
        }
        return re;
    }

    private boolean check(String word, String s) {
        int j = 0;
        for (int i = 0; i < s.length(); i++) {
            if (j < word.length() && s.charAt(i) == word.charAt(j)) {
                j++;
            } else if (i > 1 && s.charAt(i) == s.charAt(i-1) && s.charAt(i) == s.charAt(i-2)) {
                continue;
            } else if (i > 0 && i < s.length()-1 && s.charAt(i) == s.charAt(i-1) && s.charAt(i) == s.charAt(i+1)) {
                continue;
            } else {
                return false;
            }
        }
        return j == word.length();
    }
}
