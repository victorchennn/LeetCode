package Companies.Google;

public class Pow {
    public double myPow(double x, int n) {
        long N = n;
        if (n < 0) {
            x = 1/x;
            N = -N;
        }
        double re = 1;
        double prod = x;
        for (long i = N; i > 0; i/=2) {
            if (i%2 == 1) {
                re *= prod;
            }
            prod = prod * prod; // be careful
        }
        return re;
    }
}
