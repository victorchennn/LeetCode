package Companies.Microsoft;

/**
 * 1 is read off as "one 1" or 11.
 * 11 is read off as "two 1s" or 21.
 * 21 is read off as "one 2, then one 1" or 1211.
 */
public class CountAndSay {
    /**
     * "(.)": defines a group that contains a single character that could be of anything.
     * "\1": refers to the defined group with the index of 1.
     * "*": qualifier followed by the group reference \1, indicates that we would like to see the group repeats itself zero or more times.
     */
    public String countAndSay(int n) {
        String re = "1";
        for (int i = 1; i < n; i++) {
            re = helper(re);
        }

//        String regex = "(.)\\1*";
//        Pattern pattern = Pattern.compile(regex);
//        for (int i = 1; i < n; i++) {
//            Matcher m = pattern.matcher(re);
//            StringBuilder sb = new StringBuilder();
//
//            while (m.find()) {
//                sb.append(m.group().length());
//                sb.append(m.group().charAt(0));
//            }
//            re = sb.toString();
//        }

        return re;
    }

    private String helper(String s) {
        int i = 0;
        StringBuilder sb = new StringBuilder();
        while (i < s.length()) {
            int count = 0;
            char c = s.charAt(i);
            while (i < s.length() && s.charAt(i) == c) {
                i++;
                count++;
            }
            sb.append(count);
            sb.append(c);
        }
        return sb.toString();
    }
}
