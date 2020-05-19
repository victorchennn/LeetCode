package Companies.Microsoft;

public class OnebitandTwobitChar {

    /**
     * a) if there is odd amount of 1(10, ...01110, etc) the answer is false
     * b) if it's even (110, ...011110, etc) the answer is true
     */
    public boolean isOneBitCharacter(int[] bits) {
        int ones = 0;
        for (int i = bits.length - 2; i >= 0 && bits[i] != 0 ; i--) {
            ones++;
        }
        if (ones % 2 > 0) return false;
        return true;
    }
}
