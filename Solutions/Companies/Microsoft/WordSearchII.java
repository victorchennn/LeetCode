package Companies.Microsoft;

import java.util.ArrayList;
import java.util.List;

/**
 * find all words in the board.
 */
public class WordSearchII {
    private int[][] dirs = {{0,1}, {0,-1}, {-1,0}, {1, 0}};

    public List<String> findWords(char[][] board, String[] words) {
        List<String> re = new ArrayList<>();
        TrieNode root = createTrie(words);
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                dfs(re, board, root, i, j);
            }
        }
        return re;
    }

    private void dfs(List<String> re, char[][] board, TrieNode root, int x, int y) {
        char c = board[x][y];
        if (c == '$' || root.children[c-'a'] == null) {
            return;
        }
        TrieNode child = root.children[c-'a'];
        if (child.word != null) {
            re.add(child.word);
            child.word = null;  // in case "oath" and "oa"
        }
        board[x][y] = '$';
        for (int[] dir : dirs) {
            int xx = x + dir[0];
            int yy = y + dir[1];
            if (xx >= 0 && xx < board.length && yy >= 0 && yy < board[0].length) {
                dfs(re, board, child, xx, yy);
            }
        }
        board[x][y] = c;
    }

    private TrieNode createTrie(String[] words) {
        TrieNode root = new TrieNode();
        for (String word : words) {
            TrieNode cur = root;
            for (char c : word.toCharArray()) {
                if (cur.children[c-'a'] == null) {
                    cur.children[c-'a'] = new TrieNode();
                }
                cur = cur.children[c-'a'];
            }
            cur.word = word;
        }
        return root;
    }

    class TrieNode {
        TrieNode[] children = new TrieNode[26];
        String word;
    }
}
