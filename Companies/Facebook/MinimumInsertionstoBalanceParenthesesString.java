package Companies.Facebook;

/**
 * Treat '(' as openning parenthesis and '))' as closing parenthesis.
 *
 * @see ValidParentheses
 */
public class MinimumInsertionstoBalanceParenthesesString {
    public int minInsertions(String s) {
        int re = 0, needRight = 0;
        for (char c : s.toCharArray()) {
            if (c == '(') {
                if (needRight%2 > 0) {
                    needRight--;
                    re++;
                }
                needRight += 2;
            } else {
                needRight--;
                if (needRight < 0) {
                    needRight += 2;
                    re++;
                }
            }
        }
        return re + needRight;
    }
}
