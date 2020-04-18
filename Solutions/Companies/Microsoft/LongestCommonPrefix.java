package Companies.Microsoft;

public class LongestCommonPrefix {
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
