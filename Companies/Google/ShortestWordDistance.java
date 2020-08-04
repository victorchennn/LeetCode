package Companies.Google;

public class ShortestWordDistance {
    public int shortestDistance(String[] words, String word1, String word2) {
        if(word1.equals(word2)) {
            return shortest(words, word1);
        }
        int min = Integer.MAX_VALUE;
        int p1 = -1, p2 = -1;
        for(int i = 0; i < words.length; i++) {
            if(words[i].equals(word1)) {
                p1 = i;
            }
            if(words[i].equals(word2)) {
                p2 = i;
            }
            if(p1 != -1 && p2 != -1) {
                min = Math.min(min, Math.abs(p1 - p2));
            }
        }
        return min;
    }

    private int shortest(String[] words, String word) {
        int min = Integer.MAX_VALUE;
        int p = -1;
        for(int i = 0; i < words.length; i++) {
            if(!words[i].equals(word)) continue;
            if(p != -1) {
                min = Math.min(min, i - p);
            }
            p = i;
        }
        return min;
    }
}
