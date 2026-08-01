class Solution {
public:
    vector<string> restoreIpAddresses(string s) {
        vector<string> result;
        dfs(result, s, "", 0);
        return result;
    }

private:
    void dfs(vector<string>& result, const string& s, string path, int parts) {
        if (s.empty() || parts == 4) {
            if (s.empty() && parts == 4) {
                result.push_back(path.substr(1));
            }
            return;
        }

        int maxLength = (s[0] == '0') ? 1 : 3;

        for (int length = 1; length <= maxLength && length <= s.size(); ++length) {
            string number = s.substr(0, length);
            if (stoi(number) <= 255) {
                dfs(result, s.substr(length), path + "." + number, parts + 1);
            }
        }
    }
};

class Solution {
public:
    vector<string> restoreIpAddresses(string s) {
        vector<string> result;
        vector<string> parts;

        dfs(s, 0, parts, result);
        return result;
    }

private:
    void dfs(const string& s, int index, vector<string>& parts, vector<string>& result) {
        int remainingChars = s.size() - index;
        int remainingParts = 4 - parts.size();

        // 每段至少1位，最多3位
        if (remainingChars < remainingParts ||
            remainingChars > remainingParts * 3) {
            return;
        }

        if (parts.size() == 4) {
            if (index == s.size()) {
                result.push_back(
                    parts[0] + "." +
                    parts[1] + "." +
                    parts[2] + "." +
                    parts[3]
                );
            }
            return;
        }

        int value = 0;

        for (int len = 1; len <= 3 && index + len <= s.size(); ++len) {
            // 不能有前导零，例如 "01"
            if (len > 1 && s[index] == '0') {
                break;
            }

            value = value * 10 + (s[index + len - 1] - '0');
          
            if (value > 255) {
                break;
            }

            parts.push_back(s.substr(index, len));

            dfs(s, index + len, parts, result);

            parts.pop_back();
        }
    }
};
