package Companies.Bloomberg;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RemoveSpaces {
    public String remove(String s) {
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

    @Test
    void test() {
        assertEquals("GeeksforGeeks", remove("GeeksforGeeks"));
        assertEquals("GeeksforGeeks", remove("   GeeksforGeeks"));
        assertEquals("GeeksforGeeks ", remove("GeeksforGeeks   "));
        assertEquals("GeeksforGeeks. ", remove("GeeksforGeeks. "));
        assertEquals("Hello Geeks. Welcome to GeeksforGeeks. ", remove("   Hello Geeks . Welcome   to  GeeksforGeeks   .    "));
    }
}
