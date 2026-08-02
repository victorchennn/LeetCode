class Node {
public:
    int val;
    Node* next;
    Node* random;

    explicit Node(int value)
        : val(value), next(nullptr), random(nullptr) {}
};

class Solution {
public:
    Node* copyRandomList(Node* head) {
        if (head == nullptr) {
            return nullptr;
        }

        // 1. 在每个原节点后插入复制节点
        // A -> B -> C
        // 变成：
        // A -> A' -> B -> B' -> C -> C'
        Node* cur = head;

        while (cur != nullptr) {
            Node* copy = new Node(cur->val);

            copy->next = cur->next;
            cur->next = copy;

            cur = copy->next;
        }

        // 2. 设置复制节点的 random
        cur = head;
        while (cur != nullptr) {
            Node* copy = cur->next;

            if (cur->random != nullptr) {
                copy->random = cur->random->next;
            }

            cur = copy->next;
        }

        // 3. 拆分原链表和复制链表
        Node* copiedHead = head->next;
        cur = head;

        while (cur != nullptr) {
            Node* copy = cur->next;
            Node* nextOriginal = copy->next;

            // 恢复原链表
            cur->next = nextOriginal;

            // 连接复制链表
            copy->next =
                nextOriginal == nullptr
                    ? nullptr
                    : nextOriginal->next;

            cur = nextOriginal;
        }

        return copiedHead;
    }
};

#include <unordered_map>

class Solution {
private:
    std::unordered_map<Node*, Node*> visited_;

public:
    Node* copyRandomList(Node* head) {
        if (head == nullptr) {
            return nullptr;
        }

        if (visited_.contains(head)) {
            return visited_[head];
        }

        Node* copy = new Node(head->val);
        visited_[head] = copy;

        copy->next = copyRandomList(head->next);
        copy->random = copyRandomList(head->random);

        return copy;
    }
};
