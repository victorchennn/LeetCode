// template <typename T>
class Node {
public:
    string val;
    Node* next;

    explicit Node(const string& v)
        : val(v), next(nullptr) {}
};

// template <typename T>
class Stack {
private:
    Node* top_; // Node<T>*

public:
    Stack() : top_(nullptr) {}

    ~Stack() {
        while (top_) {
            Node* next = top_->next;
            delete top_;
            top_ = next;
        }
    }

    void push(const string& val) {
        Node* node = new Node(val); // Node<T>* node = new Node<T>(value);
        node->next = top_;
        top_ = node;
    }

    string pop() {
        if (empty()) {
            throw runtime_error("Stack is empty");
        }

        Node* node = top_;
        string val = node->val;

        top_ = top_->next;
        delete node;

        return val;
    }

    const string& top() const {
        if (empty()) {
            throw runtime_error("Stack is empty");
        }

        return top_->val;
    }

    bool empty() const {
        return top_ == nullptr;
    }
};
