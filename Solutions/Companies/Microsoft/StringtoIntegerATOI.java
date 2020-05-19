package Companies.Microsoft;

public class StringtoIntegerATOI {
    public int myAtoi(String str) {
        int i = 0, sign = 1, num = 0;
        while (i < str.length() && str.charAt(i) == ' ') {
            i++;
        }
        if (i == str.length()) {
            return 0;
        }
        if (str.charAt(i) == '+' || str.charAt(i) == '-') {
            sign = str.charAt(i) == '+'?1:-1;
            i++;
        }
        while (i < str.length()) {
            int digit = str.charAt(i)-'0';
            if (digit < 0 || digit > 9) {
                break;
            }
            if (num > Integer.MAX_VALUE/10 || num == Integer.MAX_VALUE/10 && digit > Integer.MAX_VALUE%10) {
                return sign == 1? Integer.MAX_VALUE:Integer.MIN_VALUE;
            }
            num = num*10+digit;
            i++;
        }
        return sign*num;
    }
}
