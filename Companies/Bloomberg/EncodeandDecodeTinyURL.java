package Companies.Bloomberg;

import java.util.HashMap;
import java.util.Random;

public class EncodeandDecodeTinyURL {

    private String ALPHABET = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private HashMap<String, String> map = new HashMap<>();
    private Random rand = new Random();

    public String encode(String longUrl) {
        String key = getKey();
        while (map.containsKey(key)) {
            key = getKey();
        }
        map.put(key, longUrl);
        return "http://tinyurl.com/" + key;
    }

    public String decode(String shortUrl) {
        return map.get(shortUrl.replace("http://tinyurl.com/", ""));
    }

    private String getKey() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            sb.append(ALPHABET.charAt(rand.nextInt(ALPHABET.length())));
        }
        return sb.toString();
    }
}
