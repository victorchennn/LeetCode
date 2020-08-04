#include <iostream>
#include <string>
#include <vector>
#include <climits>

using namespace std;

class ShortestWordDistance
{
public:
  int shortestDistance(vector<string> &words, string word1, string word2)
  {
    int p1{-1}, p2{-1}, re{INT_MAX};
    for (int i = 0; i < words.size(); i++)
    {
      if (words[i] == word1)
      {
        p1 = i;
      }
      if (words[i] == word2)
      {
        p2 = i;
      }
      if (p1 != -1 && p2 != -1)
      {
        re = min(re, abs(p1 - p2));
      }
    }
    return re;
  }
};