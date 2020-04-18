package Topics.Design;

public class CircularQueue {
    private int[] a;
    private int head, tail, len;

    /** Initialize your data structure here. Set the size of the queue to be k. */
    public CircularQueue(int k) {
        tail = -1;
        a = new int[k];
    }

    /** Insert an element into the circular queue. Return true if the operation is successful. */
    public boolean enQueue(int value) {
        if (isFull()) {
            return false;
        }
        tail = (tail+1)%a.length;
        a[tail] = value;
        len++;
        return true;
    }

    /** Delete an element from the circular queue. Return true if the operation is successful. */
    public boolean deQueue() {
        if (isEmpty()) {
            return false;
        }
        head = (head+1)%a.length;
        len--;
        return true;
    }

    /** Get the front item from the queue. */
    public int Front() {
        return isEmpty()?-1:a[head];
    }

    /** Get the last item from the queue. */
    public int Rear() {
        return isEmpty()?-1:a[tail];
    }

    /** Checks whether the circular queue is empty or not. */
    public boolean isEmpty() {
        return len == 0;
    }

    /** Checks whether the circular queue is full or not. */
    public boolean isFull() {
        return len == a.length;
    }
}
