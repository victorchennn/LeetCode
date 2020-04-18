package Companies.Google;

import java.util.List;

public class LongestWordinDictThroughDeleting {
    public String findLongestWord(String s, List<String> d) {
        String re = "";
        for (String ss : d) {
            if (isSeq(ss, s)) {
                if (ss.length() > re.length() || (ss.length() == re.length() && ss.compareTo(re) < 0)) {
                    re = ss;
                }
            }
        }
        return re;
    }

    private boolean isSeq(String ss, String s) {
        int j = 0;
        for (int i = 0; i < s.length() && j < ss.length(); i++) {
            if (s.charAt(i) == ss.charAt(j)) {
                j++;
            }
        }
        return j == ss.length();
    }
}
