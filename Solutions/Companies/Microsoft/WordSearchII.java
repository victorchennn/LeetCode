package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

public class WordSearchII {
    public List<String> findWords(char[][] board, String[] words) {
        List<String> l = new ArrayList<>();
        TrieNode root = createTrie(words);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs(l, board, root, i, j);
            }
        }
        return l;
    }

    private void dfs(List<String> l, char[][] board, TrieNode root, int i, int j) {
        char c = board[i][j];
        if (c == '$' || root.children[c-'a'] == null) {
            return;
        }
        TrieNode child = root.children[c-'a'];
        if (child.word != null) {
            l.add(child.word);
            child.word = null;
        }
        board[i][j] = '$';
        if (i > 0) dfs(l, board, child, i-1, j);
        if (j > 0) dfs(l, board, child, i, j-1);
        if (i < board.length-1) dfs(l, board, child, i+1, j);
        if (j < board[0].length-1) dfs(l, board, child, i, j+1);
        board[i][j] = c;
    }

    private TrieNode createTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String w : words) {
            TrieNode cur = root;
            for (char c : w.toCharArray()) {
                if (cur.children[c-'a'] == null) {
                    cur.children[c-'a'] = new TrieNode();
                }
                cur = cur.children[c-'a'];
            }
            cur.word = w;
        }
        return root;
    }

    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word;
    }
}
