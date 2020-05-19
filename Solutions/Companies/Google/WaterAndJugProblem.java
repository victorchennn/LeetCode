package Companies.Google;

/**
 * determine whether it's possible to measure exactly z litres using these two jugs x and y.
 *
 * Bézout's identity (also called Bézout's lemma) is a theorem in the elementary theory of numbers:
 *
 * let a and b be nonzero integers and let d be their greatest common divisor.
 * Then there exist integers x and y such that ax + by = d
 * every integer of the form ax + by is a multiple of the greatest common divisor d.
 */
public class WaterAndJugProblem {
    public boolean canMeasureWater(int x, int y, int z) {
        if (x + y < z) {
            return false;
        }
        if (x == z || y == z || x+y == z) {
            return true;
        }
        return z%GCD(x, y) == 0;
    }

    private int GCD(int x, int y) {
        if (y == 0) {
            return x;
        }
        return GCD(y, x%y);
    }
}
