package Companies.Bloomberg;

public class DivideTwoIntegers {
    public int divide(int dividend, int divisor) {
        if (dividend == Integer.MIN_VALUE && divisor == -1) {
            return Integer.MAX_VALUE;
        }
//        if (dividend == 1<<31 && divisor == -1) {
//            return (1<<31)-1;
//        }

        int a = Math.abs(dividend), b = Math.abs(divisor), re = 0;
        for (int i = 31; i >= 0; i--) {
            if ((a >>> i) - b >= 0) {
                re += 1 << i;
                a -= b << i;
            }
        }
        return (dividend > 0) == (divisor > 0)? re:-re;
    }
}
