package Companies.Bloomberg;

public class FindMostFrequentCharinStr {
    public static char find(String s) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']++;
        }
        int max = 0;
        char c = s.charAt(0);
        for (int i = 0; i < count.length; i++) {
            if (count[i] > max) {
                max = count[i];
                c = (char)('a'+i);
            }
        }
        return c;
    }

    public static void main(String...args) {
        System.out.println(find("bloomberg"));
        System.out.println(find("blooomberg"));
    }
}
