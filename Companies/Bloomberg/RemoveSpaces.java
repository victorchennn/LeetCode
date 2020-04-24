package Companies.Bloomberg;

public class RemoveSpaces {
    public static String remove(String s) {
        int i = 0, j = 0;
        boolean found = false;
        char[] ch = s.toCharArray();
        while (j < s.length() && ch[j] == ' ') {
            j++;
        }
        while (j < s.length()) {
            if (ch[j] != ' ') {
                if ((ch[j] == '.' || ch[j] == ',' || ch[j] == '?') && i >= 1 && ch[i-1] == ' ') {
                    ch[i-1] = ch[j++];
                } else {
                    ch[i++] = ch[j++];
                }
                found = false;
            } else {
                j++;
                if (!found) {
                    ch[i++] = ' ';
                    found = true;
                }
            }
        }
        return new String(ch, 0, i);
    }

    public static void main(String...args) {
        System.out.println(remove("GeeksforGeeks"));
        System.out.println(remove("   GeeksforGeeks"));
        System.out.println(remove("GeeksforGeeks   "));
        System.out.println(remove("GeeksforGeeks. "));
        System.out.println(remove("   Hello Geeks . Welcome   to  GeeksforGeeks   .    "));
    }
}
