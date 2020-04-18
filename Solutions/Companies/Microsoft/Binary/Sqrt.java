package Companies.Microsoft.Binary;

public class Sqrt {
    public int mySqrt(int x) {
        if (x < 2) {
            return x;
        }
        int left = mySqrt(x >> 2) << 1;
        int right = left+1;
        return (long)right * right > x ? left:right;
    }

    public int mySqrtII(int x) {
        if (x < 2) return x;
        long num;
        int pivot, left = 2, right = x / 2;
        while (left <= right) {
            pivot = left + (right - left) / 2;
            num = (long)pivot * pivot;
            if (num > x) right = pivot - 1;
            else if (num < x) left = pivot + 1;
            else return pivot;
        }
        return right;
    }
}
