public int consecutiveNumbersSum(int n) {
    int re = 0;
    for (int k = 1; k*(k+1) <= 2*n; k++)
    {
        if (2*n%k == 0 && (2*n/k-k+1)%2 == 0)
        {
            re++;
        }
    }
    return re;
}

// (a+a+k-1)*k/2 = n
// a >= 1
// 2n%k == 0
// (2n/k-k+1)%2 == 0
