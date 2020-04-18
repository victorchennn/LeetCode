package Companies.Tencent;

public class IntegerBreak {
    public int integerBreak(int n) {
        if (n == 2 || n == 3) {
            return n-1;
        }
        int re = 1;
        while (n > 4) {
            re *= 3;
            n -=3;
        }
        re *= n;
        return re;
    }
}
