package Companies.Twitter;

public class Anagram {
    public int count(String s1, String s2) {
        if (s1.length() != s2.length()) {
            return -1;
        }
        int count = 0;
        int char_count[] = new int[26];
        for (int i = 0; i < s1.length(); i++)
            char_count[s1.charAt(i) - 'a']++;
        for (int i = 0; i < s2.length(); i++)
            if (char_count[s2.charAt(i) - 'a']-- <= 0)
                count++;
        return count;
    }

    public static void main(String[] args) {
        Anagram test = new Anagram();
//        System.out.println(test.count("toe", "ate"));
//        System.out.println(test.count("a", "bb"));
//        System.out.println(test.count("jk", "kj"));
//        System.out.println(test.count("abb", "bbc"));
//        System.out.println(test.count("mn", "op"));
//        System.out.println(test.count("abc", "def"));
    }
}
