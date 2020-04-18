package Companies.Twitter;

import java.util.HashSet;
import java.util.Set;

public class PalindromicSub {
    public int countSubstrings(String s) {
        int count=0;
        Set<String> set = new HashSet<>();
        for(int i=0;i<s.length();i++){
            count+=extractPalindrome(s,i,i, set);//odd length
            count+=extractPalindrome(s,i,i+1, set);//even length
        }
        return count;
    }

    public int extractPalindrome(String s, int left, int right, Set<String> set){
        int count=0;
        while(left>=0 && right<s.length()&& (s.charAt(left)==s.charAt(right))){
            String sub = s.substring(left, right+1);
            if (!set.contains(sub)) {
                set.add(sub);
                count++;
            }
            left--;
            right++;
        }
        return count;
    }

    public static void main(String...args) {
        PalindromicSub test = new PalindromicSub();
        System.out.println(test.countSubstrings("aaa"));
        System.out.println(test.countSubstrings("aabaa"));
        System.out.println(test.countSubstrings("mokkori"));
    }
}
