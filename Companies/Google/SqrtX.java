package Companies.Google;

public class SqrtX {
    public int mySqrt(int x) {
        if (x < 2) {
            return x;
        }
        int left = mySqrt(x >> 2) << 1;
        int right = left+1;
        return (long)right*right > x? left:right;
    }
}
