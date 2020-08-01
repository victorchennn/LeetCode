#include <iostream>
#include <vector>
#include <queue>
#include <set>

using namespace std;

class MedianFinder
{
  vector<int> store{};

  priority_queue<int> lo{};                            // max heap
  priority_queue<int, vector<int>, greater<int>> hi{}; // min heap

  multiset<int> data;
  multiset<int>::iterator mid;

public:
  /** initialize your data structure here. */
  MedianFinder() : mid(data.end())
  {
  }

  void addNum(int num)
  {
    // insertion sort, O(n) time to insert since elements have to be shifted
    // inside the container to make room for the new element.
    if (store.empty())
      store.push_back(num);
    else
      store.insert(lower_bound(store.begin(), store.end(), num), num);

    // two heaps, O(5logn) at worst with three insertions and two deletions from
    // the heaps.
    lo.push(num);
    hi.push(lo.top());
    lo.pop();
    if (lo.size() < hi.size())
    {
      lo.push(hi.top());
      hi.pop();
    }

    // multiset and pointer, O(logn) for multiset insertion.
    const int n = data.size();
    data.insert(num);
    if (!n)
      mid = data.begin();
    else if (num < *mid)
      mid = (n & 1 ? mid : prev(mid));
    else
      mid = (n & 1 ? next(mid) : mid);
  }

  double findMedian()
  {
    // const int n = store.size();
    // return n & 1 ? store[n / 2] : (static_cast<double>(store[n / 2]) + store[n / 2 - 1]) * 0.5;

    // return lo.size() > hi.size() ? lo.top() : (lo.top() + hi.top()) * 0.5;

    const int n = store.size();
    return (*mid + *next(mid, n % 2 - 1)) * 0.5;
  }
};
