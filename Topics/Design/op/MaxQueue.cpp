class MaxQueue {
private:
    queue<int> q;
    deque<int> maxDeque; // decreasing

public:
    void push(int x) {
        q.push(x);

        // 比 x 小的元素以后不可能成为 max
        while (!maxDeque.empty() && maxDeque.back() < x) {
            maxDeque.pop_back();
        }

        maxDeque.push_back(x);
    }

    void pop() {
        if (q.empty()) {
            throw runtime_error("queue is empty");
        }

        int x = q.front();
        q.pop();

        // 如果真正出队的元素刚好是当前最大值
        if (x == maxDeque.front()) {
            maxDeque.pop_front();
        }
    }

    int get_max() const {
        if (q.empty()) {
            throw runtime_error("queue is empty");
        }

        return maxDeque.front();
    }

    int front() const {
        if (q.empty()) {
            throw runtime_error("queue is empty");
        }

        return q.front();
    }

    bool empty() const {
        return q.empty();
    }
};

int main() {
    MaxQueue q;

    q.push(3);
    q.push(1);
    q.push(5);
    q.push(2);

    cout << q.get_max() << '\n'; // 5

    q.pop(); // remove 3
    cout << q.get_max() << '\n'; // 5

    q.pop(); // remove 1
    cout << q.get_max() << '\n'; // 5

    q.pop(); // remove 5
    cout << q.get_max() << '\n'; // 2
}
