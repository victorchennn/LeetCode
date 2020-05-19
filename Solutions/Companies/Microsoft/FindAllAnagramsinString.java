package Companies.Microsoft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FindAllAnagramsinString {
    public List<Integer> findAnagrams(String s, String p) {
        if (s.length() < p.length()) {
            return new ArrayList<>();
        }
        int[] count = new int[256];
        for (char c : p.toCharArray()) {
            count[c]++;
        }
        int match = 0, l = 0, r = 0;
        List<Integer> re = new ArrayList<>();
        while (r < s.length()) {
            if (count[s.charAt(r)] > 0) {
                count[s.charAt(r)]--;
                r++;
                match++;
            } else {
                count[s.charAt(l)]++;
                l++;
                match--;
            }
            if (match == p.length()) {
                re.add(l);
            }
        }
        return re;
    }

    public List<Integer> findAnagramsII(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        if (sLen < pLen) {
            return new ArrayList<>();
        }
        int[] pCount = new int[26];
        int[] sCount = new int[26];
        for (char c : p.toCharArray()) {
            pCount[c-'a']++;
        }
        List<Integer> re = new ArrayList<>();
        for (int i = 0; i < sLen; i++) {
            sCount[s.charAt(i)-'a']++;
            if (i >= pLen) {
                sCount[s.charAt(i-pLen)-'a']--;
            }
            if (Arrays.equals(pCount, sCount)) {
                re.add(i-pLen+1);
            }
        }
        return re;
    }
}
