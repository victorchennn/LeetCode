package Companies.Amazon;

public class ReverseWordsinaString {

//    Input: "the sky is blue"
//    Output: "blue is sky the"
    public String reverseWords(String s) {
        if (s.length() == 0) {
            return s;
        }
        String[] ss = s.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = ss.length-1; i >= 0; i--) {
            if (ss[i].length() > 0) { // !!!
                sb.append(ss[i] + " ");
            }
        }
        return sb.toString().trim();
    }

//    Input:  ["t","h","e"," ","s","k","y"," ","i","s"," ","b","l","u","e"]
//    Output: ["b","l","u","e"," ","i","s"," ","s","k","y"," ","t","h","e"]
    public void reverseWords(char[] s) {
        reverse(s, 0, s.length-1);
        int start = 0;
        for (int i = 0; i < s.length; i++) {
            if (s[i] == ' ') {
                reverse(s, start, i-1);
                start = i+1;
            }
        }
        reverse(s, start, s.length-1);
    }

//    Input: "Let's take LeetCode contest"
//    Output: "s'teL ekat edoCteeL tsetnoc"
    public String reverseWordsIII(String s) {
        int start = 0;
        char[] ss = s.toCharArray();
        for (int i = 0; i < s.length(); i++) {
            if (ss[i] == ' ') {
                reverse(ss, start, i-1);
                start = i+1;
            }
        }
        reverse(ss, start, ss.length-1);
        return new String(ss);
    }

//    public String reverseWordsIII(String s) {
//        StringBuilder sb = new StringBuilder();
//        String[] sl = s.split(" ");
//        for (String each : sl) {
//            sb.append(new StringBuilder(each).reverse());
//            sb.append(" ");
//        }
//        sb.deleteCharAt(sb.length()-1);
//        return sb.toString();
//    }

    private void reverse(char[] s, int l, int r) {
        while (l < r) {
            char temp = s[r];
            s[r] = s[l];
            s[l] = temp;
            l++;
            r--;
        }
    }
}

