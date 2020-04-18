package Libs;

import java.util.Arrays;

public class PlayGround {

    public static void main(String...args) {
        PlayGround pg = new PlayGround();

        String s1 = "1{2{3{4}}5}}6";
        String s2 = "{{{444}}{}{}}";
        System.out.println(pg.find(s1));
        System.out.println(pg.find(s2));

        pg.subset("aabc");
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

    private String find(String s) {
        int counter = 0, max = 0, l = 0, r = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '{') {
                counter++;
                if (counter > max) {
                    max = counter;
                    l = i;
                }
            } else if (c == '}') {
                counter--;
            }
        }
        r = l;
        while (s.charAt(r) != '}') {
            r++;
        }
        return s.substring(l+1, r);
    }
}

