package Topics.SlidingWindow;

public class LongestSubstringwithAtMostKDistinctChar {
    public int lengthOfLongestSubstringKDistinct(String s, int k) {
        int[] count = new int[256];
        int number = 0, re = 0, l = 0;
        for (int r = 0; r < s.length(); r++) {
            if (count[s.charAt(r)] == 0) {
                number++;
            }
            count[s.charAt(r)]++;
            if (number > k) {
//                count[s.charAt(l)]--;
//                while (count[s.charAt(l)] > 0) {
//                    l++;
//                    count[s.charAt(l)]--;
//                }
//                l++;
//              ------
                while (--count[s.charAt(l++)] > 0) {}

                number--;
            }
            re = Math.max(re, r-l+1);
        }
        return re;
    }
}
