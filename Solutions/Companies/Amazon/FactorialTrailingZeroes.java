package Companies.Amazon;

public class FactorialTrailingZeroes {
    public int trailingZeroes(int n) {
        int re = 0;
        while (n > 0) {
            n /= 5;
            re += n;
        }
        return re;

        // return n == 0? 0 : n/5 + trailingZeroes(n/5);
    }
}
