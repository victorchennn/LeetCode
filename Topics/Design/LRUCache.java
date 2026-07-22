#include <unordered_map>
using namespace std;

class LRUCache {
private:
    struct Node {
        int key;
        int value;
        Node* prev;
        Node* next;

        Node(int key = 0, int value = 0)
            : key(key), value(value), prev(nullptr), next(nullptr) {}
    };

    int capacity;
    Node* head;
    Node* tail;

    unordered_map<int, Node*> cache;

public:
    explicit LRUCache(int capacity)
        : capacity(capacity) {
        head = new Node();
        tail = new Node();

        head->next = tail;
        tail->prev = head;
    }

    ~LRUCache() {
        Node* current = head;

        while (current != nullptr) {
            Node* next = current->next;
            delete current;
            current = next;
        }
    }

    int get(int key) {
        auto it = cache.find(key);

        if (it == cache.end()) {
            return -1;
        }

        Node* node = it->second;
        moveToFront(node);

        return node->value;
    }

    void put(int key, int value) {
        auto it = cache.find(key);

        if (it != cache.end()) {
            Node* node = it->second;
            node->value = value;
            moveToFront(node);
            return;
        }

        Node* node = new Node(key, value);
        cache[key] = node;
        addToFront(node);

        if (cache.size() > static_cast<size_t>(capacity)) {
            Node* removed = removeLast();
            cache.erase(removed->key);
            delete removed;
        }
    }

private:
    void removeNode(Node* node) {
        node->prev->next = node->next;
        node->next->prev = node->prev;
    }

    void addToFront(Node* node) {
        node->next = head->next;
        node->prev = head;

        head->next->prev = node;
        head->next = node;
    }

    void moveToFront(Node* node) {
        removeNode(node);
        addToFront(node);
    }

    Node* removeLast() {
        Node* node = tail->prev;
        removeNode(node);
        return node;
    }
};
