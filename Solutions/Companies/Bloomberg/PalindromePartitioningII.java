package Companies.Bloomberg;

import java.util.stream.IntStream;

public class PalindromePartitioningII {
    public int minCut(String s) {
        if (s.length() <= 1) {
            return 0;
        }
        int[] cut = IntStream.range(0, s.length()).toArray(); // for cut[], 1char need 0cut, 2char need 1cut, 3char need 2cut....
        for (int mid = 1; mid < s.length(); mid++) {
            for (int l = mid, r = mid; l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r); l--, r++) {
                int newcut = (l == 0?0:cut[l-1]+1);
                cut[r] = Math.min(cut[r], newcut);
            }
            for (int l = mid-1, r = mid; l >= 0 && r < s.length() && s.charAt(l) == s.charAt(r); l--, r++) {
                int newcut = (l == 0?0:cut[l-1]+1);
                cut[r] = Math.min(cut[r], newcut);
            }
        }
        return cut[s.length()-1];
    }
}
