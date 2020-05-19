package Topics.Design;

import java.util.NoSuchElementException;

public class Flatten2DVector {
    private int currow, curcol, m;
    private int[][] v;

    public Flatten2DVector(int[][] v) {
        m = v.length;
        this.v = v;
    }

    public int next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        return v[currow][curcol++];
    }

    public boolean hasNext() {
        if (currow == m) {
            return false;
        }
        if (v[currow].length == curcol) {
            currow++;
            curcol = 0;
            return hasNext();
        }
        return true;
    }
}
