package Topics.Design;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedBlockingQueue {

    private ReentrantLock lock = new ReentrantLock();
    private Condition full = lock.newCondition();
    private Condition notFull = lock.newCondition();
    private int[] queue;
    private int head = 0, tail = 0, size = 0;

    public BoundedBlockingQueue(int capacity) {
        queue = new int[capacity];
    }

    public void enqueue(int element) throws InterruptedException{
        lock.lock();
        try {
            while (size == queue.length) {
                full.await();
            }
            queue[tail++] = element;
            tail %= queue.length;
            size++;
            notFull.signal();
        } finally {
            lock.unlock();
        }
    }

    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (size == 0) {
                notFull.await();
            }
            int re = queue[head++];
            head %= queue.length;
            size--;
            full.signal();
            return re;
        } finally {
            lock.unlock();
        }
    }

    public int size() throws InterruptedException {
        lock.lock();
        try {
            return this.size;
        } finally {
            lock.unlock();
        }
    }
}
