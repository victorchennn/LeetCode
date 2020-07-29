#include <iostream>
#include <vector>

using namespace std;

class SquaresofSortedArray
{
public:
  vector<int> sortedSquares(vector<int> &A)
  {
    vector<int> re(A.size());
    int l = 0, r = A.size() - 1;
    for (int i{r}; i >= 0; i--)
    {
      re[i] = abs(A[l]) >= abs(A[r]) ? A[l] * A[l++] : A[r] * A[r--];
    }
    return re;
  }
};