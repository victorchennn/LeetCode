// 字符串的encode和discode "aaabbbb"<=>"a3b4"
string encode(const string& s) {
    if (s.empty()) return "";

    string res;
    int count = 1;

    for (int i = 1; i <= s.size(); i++) {
        if (i < s.size() && s[i] == s[i - 1]) {
            count++;
        } else {
            res += s[i - 1];
            res += to_string(count);
            count = 1;
        }
    }

    return res;
}

string decode(const string& s) {
    string res;

    for (int i = 0; i < s.size();) {
        char c = s[i++];

        int count = 0;
        while (i < s.size() && isdigit(s[i])) {
            count = count * 10 + (s[i] - '0');
            i++;
        }

        res.append(count, c);
    }

    return res;
}
