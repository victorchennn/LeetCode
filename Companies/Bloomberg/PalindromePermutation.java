package Companies.Bloomberg;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PalindromePermutation {
    public boolean canPermutePalindrome(String s) {
        Set<Character> set = new HashSet<>();
        for(char c : s.toCharArray()) {
            if (!set.add(c)) {
                set.remove(c);
            }
        }
        return set.size() <= 1;
    }

    public List<String> generatePalindromes(String s) {
        List<String> re = new ArrayList<>();
        int[] count = new int[256];
        int odd = 0;
        for (char c : s.toCharArray()) {
            count[c]++;
            if (count[c]%2 == 0) {
                odd--;
            } else {
                odd++;
            }
        }
        StringBuilder sb = new StringBuilder();
        if (odd <= 1) {
            for (int i = 0; i < count.length; i++) {
                if (count[i]%2 == 1) {
                    sb.append((char)i);
                    count[i]--;
                    break;
                }
            }
            helper(re, sb, s.length(), count);
        }
        return re;
    }

    private void helper(List<String> re, StringBuilder sb, int len, int[] count) {
        if (len == sb.length()) {
            re.add(sb.toString());
            return;
        }
        for (int i = 0; i < count.length; i++) {
            if (count[i] > 0) {
                count[i] -= 2;
                sb.insert(0, (char)i);
                sb.append((char)i);
                helper(re, sb, len, count);
                sb.deleteCharAt(0);
                sb.deleteCharAt(sb.length()-1);
                count[i] += 2;
            }
        }

    }
}
