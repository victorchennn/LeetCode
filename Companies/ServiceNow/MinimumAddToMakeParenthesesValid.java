package Companies.ServiceNow;

public class MinimumAddToMakeParenthesesValid {
    public int minAddToMakeValid(String S) {
        int re = 0, open = 0;
        for (char c : S.toCharArray()) {
            open += c == '('? 1 : -1;
            if (open == -1) {
                open++;
                re++;
            }
        }
        return re+open;
    }
}
