// delete? Instead of removing immediately, mark the value as "deleted". unordered_map<int, int> delayed;
// while (!lo.empty() &&  delayed[lo.top()] > 0) { 
//     delayed[lo.top()]--;
//     lo.pop(); // The deleted value disappears only when it reaches the top.
// }

// heaps are faster contiguous vector, multiset is slower as a Red-Black Tree.

class MedianFinder {
private:
    // Smaller half. The largest value is at the top.
    std::priority_queue<int> lower;

    // Larger half. The smallest value is at the top.
    std::priority_queue<
        int,
        std::vector<int>,
        std::greater<int>
    > upper;

public:
    void addNum(int num) {
        lower.push(num);

        upper.push(lower.top());
        lower.pop();

        if (lower.size() < upper.size()) {
            lower.push(upper.top());
            upper.pop();
        }
    }

    double findMedian() const {
        if (lower.size() > upper.size()) {
            return static_cast<double>(lower.top());
        }

        return (
            static_cast<double>(lower.top()) +
            static_cast<double>(upper.top())
        ) / 2.0;
    }
};
