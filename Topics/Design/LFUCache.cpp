#include <list>
#include <unordered_map>
using namespace std;

class LFUCache {
private:
    struct FrequencyNode {
        int frequency;
        list<int> keys;  // front = least recently used
        FrequencyNode* prev = nullptr;
        FrequencyNode* next = nullptr;

        explicit FrequencyNode(int frequency)
            : frequency(frequency) {}
    };

    struct KeyInfo {
        int value;
        FrequencyNode* frequencyNode;
        list<int>::iterator keyIterator;
    };

    int capacity;
    FrequencyNode* head = nullptr;

    // key -> value, frequency node, position in that node's key list
    unordered_map<int, KeyInfo> keyMap;

public:
    explicit LFUCache(int capacity)
        : capacity(capacity) {}

    ~LFUCache() {
        while (head != nullptr) {
            FrequencyNode* next = head->next;
            delete head;
            head = next;
        }
    }

    int get(int key) {
        auto it = keyMap.find(key);

        if (it == keyMap.end()) {
            return -1;
        }

        updateFrequency(key);
        return keyMap[key].value;
    }

    void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        auto it = keyMap.find(key);

        // Existing key
        if (it != keyMap.end()) {
            it->second.value = value;
            updateFrequency(key);
            return;
        }

        // Cache full: remove LFU + LRU key
        if (keyMap.size() == static_cast<size_t>(capacity)) {
            removeHeadKey();
        }

        addNewKey(key, value);
    }

private:
    void updateFrequency(int key) {
        auto& info = keyMap[key];

        FrequencyNode* current = info.frequencyNode;
        int nextFrequency = current->frequency + 1;

        // Remove key from current frequency list
        current->keys.erase(info.keyIterator);

        FrequencyNode* target;

        // Create the next frequency node if it does not exist
        if (current->next == nullptr ||
            current->next->frequency != nextFrequency) {
            target = new FrequencyNode(nextFrequency);
            insertAfter(current, target);
        } else {
            target = current->next;
        }

        // Recently accessed key goes to the back
        target->keys.push_back(key);

        info.frequencyNode = target;
        info.keyIterator = prev(target->keys.end());

        // Remove empty frequency node
        if (current->keys.empty()) {
            removeNode(current);
        }
    }

    void addNewKey(int key, int value) {
        // New keys start with frequency 1
        if (head == nullptr || head->frequency != 1) {
            FrequencyNode* newHead = new FrequencyNode(1);

            newHead->next = head;

            if (head != nullptr) {
                head->prev = newHead;
            }

            head = newHead;
        }

        head->keys.push_back(key);

        keyMap[key] = {
            value,
            head,
            prev(head->keys.end())
        };
    }

    void removeHeadKey() {
        // head has the lowest frequency.
        // Its front key is the least recently used among that frequency.
        int keyToRemove = head->keys.front();
        head->keys.pop_front();

        keyMap.erase(keyToRemove);

        if (head->keys.empty()) {
            removeNode(head);
        }
    }

    void insertAfter(FrequencyNode* current,
                     FrequencyNode* newNode) {
        newNode->prev = current;
        newNode->next = current->next;

        if (current->next != nullptr) {
            current->next->prev = newNode;
        }

        current->next = newNode;
    }

    void removeNode(FrequencyNode* node) {
        if (node->prev != nullptr) {
            node->prev->next = node->next;
        } else {
            head = node->next;
        }

        if (node->next != nullptr) {
            node->next->prev = node->prev;
        }

        delete node;
    }
};
