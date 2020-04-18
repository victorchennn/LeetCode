package Companies.Tencent;

public class MinimumFactorization {
    public int smallestFactorization(int a) {
        if (a < 10) {
            return a;
        }
        long re = 0, mul = 1;
        for (int i = 9; i > 1; i--) {
            while (a % i == 0) {
                re += i*mul;
                a /= i;
                mul *= 10;
            }
        }
        return a == 1 && re <= Integer.MAX_VALUE? (int)re:0;
    }
}
