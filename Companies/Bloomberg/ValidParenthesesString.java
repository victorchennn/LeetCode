package Companies.Bloomberg;

/**
 * Let min, max respectively be the smallest and largest possible number
 * of open left brackets after processing the current character in the string.
 *
 * If we encounter a left bracket (c == '('), then min++, otherwise we could write a right bracket,
 * so min--. If we encounter what can be a left bracket (c != ')'), then max++, otherwise we must write
 * a right bracket, so max--. If max < 0, then the current prefix can't be made valid no matter what
 * our choices are. Also, we can never have less than 0 open left brackets. At the end, we should check
 * that we can have exactly 0 open left brackets.
 *
 */
public class ValidParenthesesString {
    public boolean checkValidString(String s) {
        int min = 0, max = 0;
        for (char c : s.toCharArray()) {
            min += c == '('?1:-1;
            max += c != ')'?1:-1;
            if (max < 0) {
                return false;
            }
            min = Math.max(min, 0);
        }
        return min == 0;
    }
}
