package Topics.Design;

import java.util.*;

/**
 * TreeMap:
 * Time Complexity: O(logN) for all operations except top() is O(1)
 * Space Complexity: O(N)
 *
 * TwoStacks:
 * Time Complexity: O(1) for all operations except popMax() is O(N)
 * Space Complexity: O(N)
 */
public class MaxStack {
    struct Node {
        int value;
        Node* prev;
        Node* next;

        explicit Node(int value = 0)
            : value(value), prev(nullptr), next(nullptr) {}
    };

    Node* head_;
    Node* tail_;

    // value -> 所有值等于 value 的节点
    // vector 中的顺序就是这些节点被 push 的顺序
    std::map<int, std::vector<Node*>> nodes_;

    void removeNode(Node* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }

public:
    MaxStack() {
        head_ = new Node();
        tail_ = new Node();

        head_->next = tail_;
        tail_->prev = head_;
    }

    ~MaxStack() {
        Node* current = head_;

        while (current != nullptr) {
            Node* next = current->next;
            delete current;
            current = next;
        }
    }

    void push(int x) {
        Node* node = new Node(x);

        // 插到链表头部，表示栈顶
        node->next = head_->next;
        node->prev = head_;

        head_->next->prev = node;
        head_->next = node;

        nodes_[x].push_back(node);
    }

    int pop() {
        Node* node = head_->next;
        int value = node->value;

        removeNode(node);

        auto& sameValueNodes = nodes_[value];
        sameValueNodes.pop_back();

        if (sameValueNodes.empty()) {
            nodes_.erase(value);
        }

        delete node;
        return value;
    }

    int top() const {
        return head_->next->value;
    }

    int peekMax() const {
        return nodes_.rbegin()->first;
    }

    int popMax() {
        auto maxIterator = std::prev(nodes_.end());
        int maxValue = maxIterator->first;

        auto& sameValueNodes = maxIterator->second;

        // 同一个最大值中，最后 push 的最靠近栈顶
        Node* maxNode = sameValueNodes.back();
        sameValueNodes.pop_back();

        removeNode(maxNode);

        if (sameValueNodes.empty()) {
            nodes_.erase(maxIterator);
        }

        delete maxNode;
        return maxValue;
    }
};



class MaxStack {
private:
    std::stack<int> stack_;
    std::stack<int> maxStack_;

public:
    void push(int x) {
        stack_.push(x);

        if (maxStack_.empty() || x >= maxStack_.top()) {
            maxStack_.push(x);
        } else {
            maxStack_.push(maxStack_.top());
        }
    }

    int pop() {
        int value = stack_.top();
        stack_.pop();
        maxStack_.pop();
        return value;
    }

    int top() const {
        return stack_.top();
    }

    int peekMax() const {
        return maxStack_.top();
    }

    int popMax() {
        int maxValue = peekMax();
        std::stack<int> temp;

        // 把最大值上面的元素临时取出
        while (top() != maxValue) {
            temp.push(pop());
        }

        // 删除最靠近栈顶的最大值
        pop();

        // 把临时元素放回去
        while (!temp.empty()) {
            push(temp.top());
            temp.pop();
        }

        return maxValue;
    }
};
