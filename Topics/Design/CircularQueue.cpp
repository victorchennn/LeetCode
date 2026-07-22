class MyCircularQueue {
private:
    std::vector<int> data;
    int head;
    int tail;
    int len;

public:
    MyCircularQueue(int k)
        : data(k), head(0), tail(-1), len(0) {}

    bool enQueue(int value) {
        if (isFull()) {
            return false;
        }

        tail = (tail + 1) % data.size();
        data[tail] = value;
        ++len;
        return true;
    }

    bool deQueue() {
        if (isEmpty()) {
            return false;
        }

        head = (head + 1) % data.size();
        --len;
        return true;
    }

    int Front() {
        return isEmpty() ? -1 : data[head];
    }

    int Rear() {
        return isEmpty() ? -1 : data[tail];
    }

    bool isEmpty() const {
        return len == 0;
    }

    bool isFull() const {
        return len == static_cast<int>(data.size());
    }
};
