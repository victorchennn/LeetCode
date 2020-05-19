package Topics.Design;

import java.util.Iterator;

public class PeekingIterator implements Iterator<Integer> {
    private Integer next;
    private Iterator<Integer> it;

    public PeekingIterator(Iterator<Integer> iterator) {
        // initialize any member here.
        it = iterator;
        next = iterator.next();
    }

    // Returns the next element in the iteration without advancing the iterator.
    public Integer peek() {
        return next;
    }

    // hasNext() and next() should behave the same as in the Iterator interface.
    // Override them if needed.
    @Override
    public Integer next() {
        Integer re = next;
        if (it.hasNext()) {
            next = it.next();
        } else {
            next = null;
        }
        return re;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }
}
