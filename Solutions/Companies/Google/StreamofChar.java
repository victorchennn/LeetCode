package Companies.Google;

public class StreamofChar {

    StringBuilder sb;
    Trie root;

    public void StreamChecker(String[] words) {
        root = new Trie();
        sb = new StringBuilder();
        create(words);
    }

    private void create(String[] words) {
        for (String s : words) {
            Trie cur = root;
            for (int i = s.length()-1; i >= 0; i--) {
                if (cur.children[s.charAt(i)-'a'] == null) {
                    cur.children[s.charAt(i)-'a'] = new Trie();
                }
                cur = cur.children[s.charAt(i)-'a'];
            }
            cur.isword = true;
        }
    }

    public boolean query(char letter) {
        sb.append(letter);
        Trie cur = root;
        for (int i = sb.length()-1; i >= 0 && cur != null; i--) {
            cur = cur.children[sb.charAt(i) - 'a'];
            if (cur != null && cur.isword) {
                return true;
            }
        }
        return false;
    }

    class Trie {
        Trie[] children;
        boolean isword;
        Trie() {
            children = new Trie[26];
            isword = false;
        }
    }
}
