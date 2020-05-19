package Companies.Amazon;

/**
 * @see Companies.Bloomberg.CharInStrAtoBuildStrB
 */
public class IsSubsequence {
    public boolean isSubsequence(String s, String t) {
         if (s.length() == 0) {
             return true;
         }
         int i = 0;
         for (int j = 0; j < t.length(); j++) {
             if (s.charAt(i) == t.charAt(j)) {
                 i++;
                 if (i == s.length()) {
                     return true;
                 }
             }
         }
         return false;

//        List<Integer>[] idx = new List[256];
//        for (int i = 0; i < t.length(); i++) {
//            if (idx[t.charAt(i)] == null)
//                idx[t.charAt(i)] = new ArrayList<>();
//            idx[t.charAt(i)].add(i);
//        }
//
//        int prev = 0;
//        for (int i = 0; i < s.length(); i++) {
//            if (idx[s.charAt(i)] == null) return false;
//            int j = Collections.binarySearch(idx[s.charAt(i)], prev);
//            if (j < 0) j = -j - 1;
//            if (j == idx[s.charAt(i)].size()) return false;
//            prev = idx[s.charAt(i)].get(j) + 1;
//        }
//        return true;
    }
}