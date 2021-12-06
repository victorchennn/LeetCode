package Companies.Google;

public class NumberofGoodWaystoSplitString {
    public int numSplits(String s) {
        int[] rcount = new int[26];
        int[] lcount = new int[26];
        int rdiffs = 0, ldiffs = 0, re = 0;
        for (char c : s.toCharArray()) {
            if (rcount[c-'a'] == 0) {
                rdiffs++;
            }
            rcount[c-'a']++;
        }
        for (char c : s.toCharArray()) {
            if (lcount[c-'a'] == 0) {
                ldiffs++;
            }
            lcount[c-'a']++;
            rcount[c-'a']--;
            if (rcount[c-'a'] == 0) {
                rdiffs--;
            }
            if (ldiffs == rdiffs) {
                re++;
            }
        }
        return re;
    }
}
