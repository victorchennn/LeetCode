class BasicCalculator {
public:
    int calculate(string s) {
        s.push_back('+');
        int index = 0;
        return helper(s, index);
    }

private:
    int helper(const string& s, int& index) {
        int sum = 0;
        int prev = 0;
        int num = 0;
        char sign = '+';

        while (index < s.size()) {
            char c = s[index++];

            if (c == ' ') {
                continue;
            }

            if (isdigit(c)) {
                num = num * 10 + (c - '0');
            } else if (c == '(') {
                num = helper(s, index);
            } else {
                if (sign == '+') {
                    sum += prev;
                    prev = num;
                } else {
                    sum += prev;
                    prev = -num;
                }

                if (c == ')') {
                    break;
                }

                sign = c;
                num = 0;
            }
        }

        return sum + prev;
    }
};
