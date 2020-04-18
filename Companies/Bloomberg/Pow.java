package Companies.Bloomberg;

public class Pow {
    public double myPow(double x, int n) {
        long N = n;
        if (n < 0) {
            N = -N;
            x = 1/x;
        }
        double re = 1, prod = x;
        for (long i = N; i > 0; i /= 2) {
            if (i%2 == 1) {
                re *= prod;
            }
            prod *= prod;
        }
        return re;
    }
}
