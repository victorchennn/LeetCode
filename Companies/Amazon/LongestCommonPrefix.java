package Companies.Amazon;

public class LongestCommonPrefix {
    /* O(S), where S is the sum of all characters in all strings. */
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) {
            return "";
        }
        String re = strs[0];
        for (int i = 1; i < strs.length; i++) {
            while (strs[i].indexOf(re) != 0) {
                re = re.substring(0, re.length()-1);
            }
        }
        return re;
    }
}
