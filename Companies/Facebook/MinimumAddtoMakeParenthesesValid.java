package Companies.Facebook;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @see ValidParentheses
 */
public class MinimumAddtoMakeParenthesesValid {
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

    @Test
    void test() {
        assertEquals(1, minAddToMakeValid("())"));
        assertEquals(3, minAddToMakeValid("((("));
        assertEquals(0, minAddToMakeValid("()"));
        assertEquals(4, minAddToMakeValid("()))(("));
    }
}
