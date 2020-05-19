package Companies.Amazon;

/**
 * F(k) = 0 * Bk[0] + 1 * Bk[1] + ... + (n-1) * Bk[n-1]
 * F(k-1) = 0 * Bk-1[0] + 1 * Bk-1[1] + ... + (n-1) * Bk-1[n-1]
 *        = 0 * Bk[1] + 1 * Bk[2] + ... + (n-2) * Bk[n-1] + (n-1) * Bk[0]
 *
 * F(k) - F(k-1) = Bk[1] + Bk[2] + ... + Bk[n-1] + (1-n)Bk[0]
 *               = (Bk[0] + ... + Bk[n-1]) - nBk[0]
 *               = sum - nBk[0]
 *
 * F(k) = F(k-1) + sum - nBk[0]
 *
 * k = 0 B[0] = A[0];
 * k = 1 B[0] = A[len-1];
 * k = 2 B[0] = A[len-2];
 * ...
 *
 */
public class RotateFunction {
    public int maxRotateFunction(int[] A) {
        int sum = 0, F = 0, len = A.length;
        for (int i = 0; i < len; i++) {
            sum += A[i];
            F += i*A[i];
        }
        int max = F;
        for (int i = len-1; i >= 0; i--) {
            F += sum - len*A[i];
            max = Math.max(max, F);
        }
        return max;
    }
}
