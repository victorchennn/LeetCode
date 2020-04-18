package Companies.Google;

public class MinimumWindowSubsequence {
    public String minWindow(String S, String T) {
        int i = -1;
        String re = "";
        while (true) {
            for (char c : T.toCharArray()) {
                i = S.indexOf(c, i+1);
                if (i == -1) {
                    return re;
                }
            }
            int end = ++i;
            for (int j = T.length()-1; j >= 0; j--) {
                i = S.lastIndexOf(T.charAt(j), i-1);
            }
            if (re.equals("") || end - i < re.length()) {
                re = S.substring(i, end);
            }
        }
    }
}
