package Libs;

import java.util.*;

public class PlayGround {

    public static void main(String...args) {
        PlayGround pg = new PlayGround();

//        pg.subset("aabc");

//        String url1 = "https://www.hackrank.com";
//        String url2 = "https://www.hackrank.com/problems";
//        for (String s: url2.split("/")) {
//            System.out.println(s);
//        }
//        int idx = url2.indexOf("/", 8);
//        System.out.println((idx != -1? url2.substring(0, idx) : url2).replaceAll("https://", ""));
    }

    private void subset(String s) {
        char[] ch = s.toCharArray();
        Arrays.sort(ch);
        dfs(ch, new StringBuilder(), 0);
    }

    private void dfs(char[] ch, StringBuilder sb, int start) {
        System.out.println(sb.toString());
        for (int i = start; i < ch.length; i++) {
            if (i > start && ch[i] == ch[i-1]) {
                continue;
            }
            sb.append(ch[i]);
            dfs(ch, sb, i+1);
            sb.deleteCharAt(sb.length()-1);
        }
    }
}

