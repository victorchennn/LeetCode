package Companies.Amazon;

/**
 * Minimum steps to 1
 *
 * When n is odd it can be written as n = 2k+1.
 * That is, n+1 = 2k+2 and n-1 = 2k. Then, (n+1)/2 = k+1 and (n-1)/2 = k.
 * So one of (n+1)/2 and (n-1)/2 is even, the other is odd. And the "best" case of this problem
 * is to divide as much as possible. Because of that, always pick n+1 or n-1 based on if it can
 * be divided by 4. The only special case of that is when n = 3 you would like to pick n-1 rather than n+1.
 *
 * @see Companies.Bloomberg.MinimumStepstoGenerateNumber
 */
public class IntegerReplacement {
    public int integerReplacement(int n) {
        if (n == Integer.MAX_VALUE) return 32; //n = 2^31-1;
        int count = 0;
        while (n > 1){
            if (n % 2 == 0) {
                n /= 2;
            } else{
                if ((n + 1) % 4 == 0 && (n - 1 != 2)) {
                    n++;
                } else {
                    n--;
                }
            }
            count++;
        }
        return count;
    }
}
