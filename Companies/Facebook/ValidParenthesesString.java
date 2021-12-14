package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Given a string s containing only three types of characters: '(', ')' and '*', return true if s is valid.
 *
 * '*' could be treated as a single right parenthesis ')' or a single left parenthesis '(' or an empty string "".
 *
 * '(' has 1 left bracket, '((' has 2, '(()' has 1
 * For example, if we have string '(***)', then as we parse each symbol,
 * the set of possible values for the "balance" is
 * [1] for '(';
 * [0, 1, 2] for '(*';
 * [0, 1, 2, 3] for '(**';
 * [0, 1, 2, 3, 4] for '(***',
 * and [0, 1, 2, 3] for '(***)'.
 *
 * We can prove these states always form a contiguous interval.
 * Thus, we only need to know the left and right bounds of this interval.
 * That is, we would keep those intermediate states described above as
 * [lo, hi] = [1, 1], [0, 2], [0, 3], [0, 4], [0, 3]
 *
 * Let min, max respectively be the smallest and largest possible number
 * of open left brackets after processing the current character in the string.
 *
 * If we encounter a left bracket (c == '('), then min++, otherwise we could write a right bracket,
 * so min--. If we encounter what can be a left bracket (c != ')'), then max++, otherwise we must write
 * a right bracket, so max--. If max < 0, then the current prefix can't be made valid no matter what
 * our choices are. Also, we can never have less than 0 open left brackets. At the end, we should check
 * that we can have exactly 0 open left brackets.
 *
 * @see ValidParentheses
 */
public class ValidParenthesesString {
    public boolean checkValidString(String s) {
        int min = 0, max = 0;
        for (char c : s.toCharArray()) {
            min += c == '('?1:-1;
            max += c == ')'?-1:1;
            if (max < 0) {
                return false;
            }
            min = Math.max(min, 0);
        }
        return min == 0;
    }

    @Test
    void test() {
        assertEquals(true, checkValidString("()"));
        assertEquals(true, checkValidString("(*)"));
        assertEquals(true, checkValidString("(*))"));
    }
}
