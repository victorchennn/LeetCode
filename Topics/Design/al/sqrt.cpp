class Solution {
public:
    int mySqrt(int x) {
        if (x < 2) {
            return x;
        }

        // mySqrt(x) = 2 * mySqrt(x / 4)
        int left = mySqrt(x >> 2) << 1;

        int right = left + 1;
        return 1LL * right * right > x ? left : right;
    }
};

// guess = (guess + x/guess)/2
class Solution {
public:
    int mySqrtII(int x) {
        if (x < 2) {
            return x;
        }

        int left = 2;
        int right = x / 2;

        while (left <= right) {
            int pivot = left + (right - left) / 2;
            long long num = 1LL * pivot * pivot;

            if (num > x) {
                right = pivot - 1;
            } else if (num < x) {
                left = pivot + 1;
            } else {
                return pivot;
            }
        }

        return right;
    }
};

TEST(SqrtTest, BasicCases) {
    Solution s;

    EXPECT_EQ(s.mySqrt(0), 0);
    EXPECT_EQ(s.mySqrt(1), 1);
    EXPECT_EQ(s.mySqrt(2), 1);
    EXPECT_EQ(s.mySqrt(3), 1);
    EXPECT_EQ(s.mySqrt(4), 2);
    EXPECT_EQ(s.mySqrt(8), 2);
    EXPECT_EQ(s.mySqrt(9), 3);
    EXPECT_EQ(s.mySqrt(10), 3);
    EXPECT_EQ(s.mySqrt(15), 3);
    EXPECT_EQ(s.mySqrt(16), 4);
    EXPECT_EQ(s.mySqrt(2147395599), 46339);
    EXPECT_EQ(s.mySqrt(2147395600), 46340);
    EXPECT_EQ(s.mySqrt(INT_MAX), 46340);
}
