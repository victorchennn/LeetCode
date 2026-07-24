class MyHashMap {
private:
    struct ListNode {
        int key;
        int val;
        ListNode* next;

        ListNode(int k, int v)
            : key(k), val(v), next(nullptr) {}
    };

    static constexpr int SIZE = 10000;
    std::vector<ListNode*> buckets;

    int hash(int key) const {
        return key % SIZE;
    }

    // Return the previous node of key.
    ListNode* find(ListNode* head, int key) {
        ListNode* prev = head;
        while (prev->next && prev->next->key != key) {
            prev = prev->next;
        }
        return prev;
    }

public:
    MyHashMap() : buckets(SIZE, nullptr) {}

    void put(int key, int value) {
        int idx = hash(key);

        if (!buckets[idx]) {
            buckets[idx] = new ListNode(-1, -1);   // dummy head
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            prev->next = new ListNode(key, value);
        } else {
            prev->next->val = value;
        }
    }

    int get(int key) {
        int idx = hash(key);

        if (!buckets[idx]) {
            return -1;
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            return -1;
        }

        return prev->next->val;
    }

    void remove(int key) {
        int idx = hash(key);

        if (!buckets[idx]) {
            return;
        }

        ListNode* prev = find(buckets[idx], key);

        if (!prev->next) {
            return;
        }

        ListNode* node = prev->next;
        prev->next = node->next;
        delete node;
    }

    ~MyHashMap() {
        for (auto head : buckets) {
            while (head) {
                ListNode* next = head->next;
                delete head;
                head = next;
            }
        }
    }
};
