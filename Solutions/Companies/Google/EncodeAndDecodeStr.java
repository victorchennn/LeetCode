package Companies.Google;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EncodeAndDecodeStr {

    public String encode(List<String> strs) {
        if (strs.size() == 0) {
            return Character.toString((char)257);
        }
        String d = Character.toString((char)258);
        StringBuilder sb = new StringBuilder();
        for (String s : strs) {
            sb.append(s);
            sb.append(d);
        }
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    // Decodes Companies.Amazon single string to Companies.Amazon list of strings.
    public List<String> decode(String s) {
        if (s.equals(Character.toString((char)257))) {
            return new ArrayList<>();
        }
        String d = Character.toString((char)258);
        return Arrays.asList(s.split(d, -1));
    }
}
