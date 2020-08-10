#include <iostream>
#include <vector>
#include <string>
#include <unordered_map>
#include <functional>
#include <stack>

using namespace std;

class EvaluateReversePolishNotation
{
public:
  int evalRPN(vector<string> &tokens)
  {
    unordered_map<string, function<int(int, int)>> map = {
        {"+", [](int a, int b) { return a + b; }},
        {"-", [](int a, int b) { return a - b; }},
        {"*", [](int a, int b) { return a * b; }},
        {"/", [](int a, int b) { return a / b; }}};
    stack<int> s{};
    for (string &t : tokens)
    {
      if (!map.count(t))
      {
        s.push(stoi(t));
      }
      else
      {
        int num2 = s.top();
        s.pop();
        int num1 = s.top();
        s.pop();
        s.push(map[t](num1, num2));
      }
    }
    return s.top();
  }
};