#include <list>
#include <unordered_map>
using namespace std;

class LFUCache {
private:
    struct Entry {
        int value;
        int freq;
        list<int>::iterator position;
    };

    int capacity;
    int minFreq = 0;

    unordered_map<int, Entry> entries;
    unordered_map<int, list<int>> frequencyLists; // frequency, [key1, key2]

    void increaseFrequency(int key) {
        Entry& entry = entries[key];
        int oldFreq = entry.freq;

        frequencyLists[oldFreq].erase(entry.position);

        if (frequencyLists[oldFreq].empty()) {
            frequencyLists.erase(oldFreq);

            if (minFreq == oldFreq) {
                ++minFreq;
            }
        }

        ++entry.freq;
        frequencyLists[entry.freq].push_front(key);
        entry.position = frequencyLists[entry.freq].begin();
    }

public:
    explicit LFUCache(int capacity)
        : capacity(capacity) {}

    int get(int key) {
        if (!entries.contains(key)) {
            return -1;
        }

        int value = entries[key].value;
        increaseFrequency(key);
        return value;
    }

    void put(int key, int value) {
        if (capacity == 0) {
            return;
        }

        if (entries.contains(key)) {
            entries[key].value = value;
            increaseFrequency(key);
            return;
        }

        if (entries.size() == static_cast<size_t>(capacity)) {
            int evictedKey = frequencyLists[minFreq].back();

            frequencyLists[minFreq].pop_back(); // least recently used or added removed from back

            if (frequencyLists[minFreq].empty()) {
                frequencyLists.erase(minFreq);
            }

            entries.erase(evictedKey);
        }

        minFreq = 1;
        frequencyLists[1].push_front(key); // recently used or added put it front

        entries[key] = {
            value,
            1,
            frequencyLists[1].begin()
        };
    }
};
