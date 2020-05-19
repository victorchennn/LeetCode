package Libs;

import java.util.HashMap;

public class TrieNode {

    HashMap<Character, TrieNode> children;
    HashMap<String, Integer> count;

    public TrieNode(){
        children = new HashMap<>();
        count = new HashMap<>();
    }
}
