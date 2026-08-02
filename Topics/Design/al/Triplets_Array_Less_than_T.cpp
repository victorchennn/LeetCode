// Given an array of n distinct integers, d=[d[0], d[1], … d[n-1]], and an integer threshold, t, 
// how many (a,b,c) index triplets exist that satisfy both of the following conditi‍‍‍‌‍‍‌‍‌‍‍‍‌‍‌‌‌‍‌‍ons? 
// d[a]<d[b]<d[c] && d[a]+d[b]+d[c]<=t
long long countTriplets(vector<int> d, int t) {
    sort(d.begin(), d.end());

    int n = d.size();
    long long result = 0;

    for (int a = 0; a < n - 2; ++a) {
        int b = a + 1;
        int c = n - 1;

        while (b < c) {
            long long sum = static_cast<long long>(d[a]) + d[b] + d[c];

            if (sum <= t) {
                // d[a] + d[b] + d[c] <= t
                // 那么 c 左边从 b+1 到 c 的元素也都满足
                result += c - b;
                ++b;
            } else {
                // 总和太大，需要减小最大值
                --c;
            }
        }
    }
    return result;
}

// 如果还要求 a < b < c?
long long countTriplets(const vector<int>& d, int t) {
    int n = d.size();
    long long result = 0;

    for (int a = 0; a < n; ++a) {
        for (int b = a + 1; b < n; ++b) {
            if (d[a] >= d[b]) continue;

            for (int c = b + 1; c < n; ++c) {
                if (d[b] < d[c] && static_cast<long long>(d[a]) + d[b] + d[c] <= t) {
                    ++result;
                }
            }
        }
    }
    return result;
}
