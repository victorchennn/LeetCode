#include <iostream>
#include <vector>
#include <unordered_map>
#include <queue>
#include <functional>

using namespace std;

class TopKFrequentElements
{
public:
  // maxHeap O(nlog(n-k))
  vector<int> topKFrequent(vector<int> &nums, int k)
  {
    unordered_map<int, int> count{};
    for (int num : nums)
    {
      count[num]++;
    }
    vector<int> re{};
    priority_queue<pair<int, int>> q;
    for (auto it = count.begin(); it != count.end(); it++)
    {
      q.emplace(it->second, it->first);
      if (q.size() > count.size() - k)
      {
        re.push_back(q.top().second);
        q.pop();
      }
    }
    return re;
  }

  // minHeap O(nlogk)
  vector<int> topKFrequentII(vector<int> &nums, int k)
  {
    using mapIt = unordered_map<int, size_t>::iterator;
    unordered_map<int, size_t> count{};
    for (int num : nums)
    {
      count[num]++;
    }
    priority_queue<mapIt, vector<mapIt>, function<bool(mapIt, mapIt)>> heap(
        [](mapIt lhs, mapIt rhs) {
          return lhs->second > rhs->second;
        },
        vector<mapIt>());
    for (mapIt it = count.begin(); it != count.end(); it++)
    {
      heap.push(it);
      if (heap.size() > k)
      {
        heap.pop();
      }
    }
    vector<int> re(k);
    for (int i = k - 1; i >= 0; i--)
    {
      re[i] = heap.top()->first;
      heap.pop();
    }
    return re;
  }
};