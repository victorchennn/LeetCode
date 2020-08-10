#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <queue>

using namespace std;

class TopKFrequentWords
{
public:
    vector<string> topKFrequent(vector<string> &words, int k)
    {
        unordered_map<string, int> count{};
        for (auto w : words)
        {
            count[w]++;
        }
        auto comp = [&](const pair<string, int> &x, const pair<string, int> &y) {
            return x.second == y.second ? x.first < y.first : x.second > y.second;
        };
        priority_queue<pair<string, int>, vector<pair<string, int>>, decltype(comp)> pq(comp);
        for (auto w : count)
        {
            pq.emplace(w.first, w.second);
            if (pq.size() > k)
            {
                pq.pop();
            }
        }
        vector<string> re{};
        while (!pq.empty())
        {
            re.insert(re.begin(), pq.top().first);
            pq.pop();
        }
        return re;
    }
};