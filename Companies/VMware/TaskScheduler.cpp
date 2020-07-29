#include <iostream>
#include <vector>
#include <unordered_map>

using namespace std;

class TaskScheduler
{
public:
  int leastInterval(vector<char> &tasks, int n)
  {
    unordered_map<char, int> count{};
    int most{0};
    for (auto t : tasks)
    {
      count[t]++;
      most = max(most, count[t]);
    }
    int re = (most - 1) * n + most - 1;
    for (auto t : count)
    {
      if (t.second == most)
      {
        re++;
      }
    }
    return max(static_cast<int>(tasks.size()), re);
  }
};