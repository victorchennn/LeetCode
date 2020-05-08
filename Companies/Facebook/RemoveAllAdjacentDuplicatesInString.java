package Companies.Facebook;

/**
 * @see Companies.Bloomberg.CandyCrush
 */
public class RemoveAllAdjacentDuplicatesInString {
    public String removeDuplicates(String S) {
        int p = 0;
        char[] ch = S.toCharArray();
        for (int i = 0; i < S.length(); i++, p++) {
            ch[p] = ch[i];
            if (p > 0 && ch[p] == ch[p-1]) {
                p -= 2;
            }
        }
        return new String(ch, 0, p);
    }

    /* k duplicate */
    public String removeDuplicates(String s, int k) {
        int[] count = new int[s.length()];
        int p = 0;
        char[] ch = s.toCharArray();
        for (int i = 0; i < s.length(); i++, p++) {
            ch[p] = ch[i];
            count[p] = p > 0 && ch[p] == ch[p-1]? count[p-1]+1:1;
            if (count[p] == k) {
                p -= k;
            }
        }
        return new String(ch, 0, p);
    }
}
