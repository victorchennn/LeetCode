class Solution {
public:
    std::string intToRoman(int num) {
        static const std::string M[] = {
            "", "M", "MM", "MMM"
        };

        static const std::string C[] = {
            "", "C", "CC", "CCC", "CD",
            "D", "DC", "DCC", "DCCC", "CM"
        };

        static const std::string X[] = {
            "", "X", "XX", "XXX", "XL",
            "L", "LX", "LXX", "LXXX", "XC"
        };

        static const std::string I[] = {
            "", "I", "II", "III", "IV",
            "V", "VI", "VII", "VIII", "IX"
        };

        return M[num / 1000] +
               C[(num % 1000) / 100] +
               X[(num % 100) / 10] +
               I[num % 10];
    }
};

class Solution {
public:
    std::string intToRoman(int num) {
        static const std::vector<int> values = {
            1000, 900, 500, 400,
            100, 90, 50, 40,
            10, 9, 5, 4, 1
        };

        static const std::vector<std::string> symbols = {
            "M", "CM", "D", "CD",
            "C", "XC", "L", "XL",
            "X", "IX", "V", "IV", "I"
        };

        std::string result;

        for (int i = 0; i < values.size(); ++i) {
            while (num >= values[i]) {
                result += symbols[i];
                num -= values[i];
            }
        }

        return result;
    }
};

#include <string>

class Solution {
public:
    int romanToInt(const std::string& s) {
        int result = 0;

        for (int i = static_cast<int>(s.size()) - 1; i >= 0; --i) {
            switch (s[i]) {
                case 'I':
                    result += (result >= 5 ? -1 : 1);
                    break;

                case 'V':
                    result += 5;
                    break;

                case 'X':
                    result += (result >= 50 ? -10 : 10);
                    break;

                case 'L':
                    result += 50;
                    break;

                case 'C':
                    result += (result >= 500 ? -100 : 100);
                    break;

                case 'D':
                    result += 500;
                    break;

                case 'M':
                    result += 1000;
                    break;
            }
        }

        return result;
    }
};
