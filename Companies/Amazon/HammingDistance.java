package Companies.Amazon;

/**
 * Hamming distance between two strings of equal length is the number of positions
 * at which the corresponding symbols are different, exp: 1011101 and 1001001 is 2.
 */
public class HammingDistance {
    public int hammingDistance(int x, int y) {
//        return Integer.bitCount(x^y); // count number of bit '1'

        int xor = x^y;
        int dis = 0;
        while (xor != 0) {
            if (xor%2 == 1) {
                dis++;
            }
            xor = xor >> 1;
        }
        return dis;
    }

    /**
     * Remove rightmost bit of '1'
     * x = 10001000, x-1 = 10000111, x&(x-1)=10000000
     */
    public int hammingDistanceII(int x, int y) {
        int xor = x^y;
        int dis = 0;
        while (xor != 0) {
            dis++;
            xor = xor&(xor-1);
        }
        return dis;
    }
}
