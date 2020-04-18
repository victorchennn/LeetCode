package Companies.Microsoft;

public class StringCompression {
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
}
