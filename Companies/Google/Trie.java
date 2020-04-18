package Companies.Google;

public class Trie {

    TrieNode root;

    /** Initialize your data structure here. */
    public Trie() {
        root = new TrieNode();
    }

    /** Inserts Companies.Amazon word into the trie. */
    public void insert(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            if (cur.children[c-'a'] == null) {
                cur.children[c-'a'] = new TrieNode();
            }
            cur = cur.children[c-'a'];
        }
        cur.isword = true;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode cur = root;
        for (char c : word.toCharArray()) {
            if (cur.children[c-'a'] == null) {
                return false;
            }
            cur = cur.children[c-'a'];
        }
        return cur.isword;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        TrieNode cur = root;
        for (char c : prefix.toCharArray()) {
            if (cur.children[c-'a'] == null) {
                return false;
            }
            cur = cur.children[c-'a'];
        }
        return true;
    }

    class TrieNode {
        TrieNode[] children;
        Boolean isword;

        TrieNode() {
            children = new TrieNode[26];
            isword = false;
        }
    }
}
