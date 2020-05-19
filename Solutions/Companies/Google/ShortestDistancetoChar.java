package Companies.Google;

public class ShortestDistancetoChar {
    public int[] shortestToChar(String S, char C) {
        int len = S.length(), pos = -len; // negative num
        int[] re = new int[len];
        for (int i = 0; i < len; i++) {
            if (S.charAt(i) == C) {
                pos = i;
            }
            re[i] = i-pos;
        }
        for (int i = pos-1; i >= 0; i--) {  // start from pos-1, not len-1
            if (S.charAt(i) == C) {
                pos = i;
            }
            re[i] = Math.min(re[i], pos-i);
        }
        return re;
    }
}
