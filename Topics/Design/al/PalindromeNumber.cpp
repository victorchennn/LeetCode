#include <string>

class PalindromeNumber {
public:
    bool isPalindrome(int x) {
        // 负数不是回文。
        // 除了 0 以外，末尾是 0 的数字也不可能是回文，
        // 因为回文要求开头也必须是 0。
        if (x < 0 || (x % 10 == 0 && x != 0)) {
            return false;
        }

        int reversedHalf = 0;

        // 只反转后一半数字，避免整个数字反转时溢出。
        while (x > reversedHalf) {
            reversedHalf = reversedHalf * 10 + x % 10;
            x /= 10;
        }

        // 偶数位：1221 -> x = 12, reversedHalf = 12
        // 奇数位：12321 -> x = 12, reversedHalf = 123
        return x == reversedHalf || x == reversedHalf / 10;
    }

    bool isPalindromeII(int x) {
        if (x < 0) {
            return false;
        }

        std::string s = std::to_string(x);

        int left = 0;
        int right = static_cast<int>(s.size()) - 1;

        while (left < right) {
            if (s[left] != s[right]) {
                return false;
            }

            ++left;
            --right;
        }

        return true;
    }
};
