package Companies.Google;

import java.util.Arrays;

public class FindAndReplaceinStr {
    public String findReplaceString(String S, int[] indexes, String[] sources, String[] targets) {
        int[] match = new int[S.length()];
        Arrays.fill(match, -1);
        for (int p = 0; p < indexes.length; p++) {
            int index = indexes[p];
            if (sources[p].equals(S.substring(index, index + sources[p].length()))) {
                match[index] = p;
            }
        }
        StringBuilder sb = new StringBuilder();
        int i = 0;
        while (i < S.length()) {
            if (match[i] >= 0) {
                sb.append(targets[match[i]]);
                i += sources[match[i]].length();
            } else {
                sb.append(S.charAt(i++));
            }
        }
        return sb.toString();
    }
}
