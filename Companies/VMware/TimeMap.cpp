#include <iostream>
#include <string>
#include <map>
#include <unordered_map>
#include <vector>
#include <algorithm>

using namespace std;

class TimeMap
{
public:
  unordered_map<string, map<int, string>> m{};
  // unordered_map<string, vector<pair<int, string>>> m{};

  /** Initialize your data structure here. */
  TimeMap()
  {
  }

  void set(string key, string value, int timestamp)
  {
    m[key].insert({timestamp, value});
    // m[key].push_back({timestamp, value});
  }

  string get(string key, int timestamp)
  {
    auto it = m[key].upper_bound(timestamp);
    return it == m[key].begin() ? "" : prev(it)->second;

    // auto it = upper_bound(m[key].begin(), m[key].end(),
    //                       pair<int, string>(timestamp, ""),
    //                       [](const pair<int, string> &x, const pair<int, string> &y) { return x.first < y.first; });
    // return it == m[key].begin() ? "" : prev(it)->second;
  }
};