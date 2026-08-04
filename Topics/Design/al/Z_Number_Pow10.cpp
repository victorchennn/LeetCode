// 判断一个数是不是power of 10，power可以是正数也可以是负数，例如10 = 10^1, 0.1 = 10^(-1)都算
  
class Solution { // O(|log10(x)|)
public:
    bool isPowerOf10(double x) {
        const double EPS = 1e-9;

        if (x <= 0) {
            return false;
        }

        while (x > 1.0 + EPS) {
            x /= 10.0;
        }

        while (x < 1.0 - EPS) {
            x *= 10.0;
        }

        return fabs(x - 1.0) < EPS;
    }
};

class Solution {
public:
    bool isPowerOf10(double x) {
        if (x <= 0) {
            return false;
        }

        double e = log10(x);

        return fabs(e - round(e)) < 1e-9;
    }
};

// Follow up 1：输入可能非常大（字符串） 直接判断字符串：
bool isPowerOf10(const string& s) {
    if (s == "1") return true;
    if (s.empty() || s[0] != '1') return false;

    for (int i = 1; i < s.size(); ++i)
        if (s[i] != '0')
            return false;

    return true;
}
