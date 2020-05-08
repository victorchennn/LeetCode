package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringCompression {
    /**
     * Input: ['a','a','b','b','c','c','c']
     * Output: 6, ['a','2','b','2','c','3']
     *
     * Input: ['a','b','b','c','c','c']
     * Output: 5, ['a','b','2','c','3']
     */
    public int compress(char[] chars) {
        int len = 0, i = 0;
        while (i < chars.length) {
            char cur = chars[i];
            int count = 0;
            while (i < chars.length && chars[i] == cur) {
                i++;
                count++;
            }
            chars[len++] = cur;
            if (count > 1) {
                for (char c : String.valueOf(count).toCharArray()) {
                    chars[len++] = c;
                }
            }
        }
        return len;
    }

    public String deCompress(char[] chars) {
        StringBuilder sb = new StringBuilder();
        int i = 0, len = chars.length, num = 0;
        while (i < len) {
            char c = chars[i];
            if ((i == len-1) || ((i+1) < len && !Character.isDigit(chars[i+1]))) {
                sb.append(c);
                i++;
                continue;
            }
            i++;
            while (i < chars.length && Character.isDigit(chars[i])) {
                num = num*10 + chars[i]-'0';
                i++;
            }
            while (num > 0) {
                sb.append(c);
                num--;
            }
        }
        return sb.toString();
    }

    @Test
    void test() {
        assertEquals(1, compress(new char[]{'a'}));
        assertEquals(6, compress(new char[]{'a','a','b','b','c','c','c'}));
        assertEquals(5, compress(new char[]{'a','b','b','c','c','c'}));

        assertEquals("a", deCompress(new char[]{'a'}));
        assertEquals("abbccc", deCompress(new char[]{'a','b','2','c','3'}));
        assertEquals("aabbccc", deCompress(new char[]{'a','2','b','2','c','3'}));
    }
}