#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

class Solution
{
public:
  /**
   * Reorders the elements in the range [first, last) in such a way that all elements 
   * for which the predicate p returns true precede the elements for which predicate 
   * p returns false. Relative order of the elements is not preserved.
   */
  vector<int> sortArrayByParity(vector<int> &A)
  {
    auto pred = [](auto e) { return (e & 1) == 0; };
    partition(A.begin(), A.end(), pred);
    return A;
  }

  vector<int> sortArrayByParityII(vector<int> &A)
  {
    sort(A.begin(), A.end(), [](int i, int j) { return (i & 1) < (j & 1); });
    return A;
  }

  vector<int> sortArrayByParityIII(vector<int> &A)
  {
    for (int i{0}, j{0}; j < A.size(); j++)
    {
      if ((A[j] & 1) == 0)
      {
        swap(A[i++], A[j]);
      }
    }
    return A;
  }
};