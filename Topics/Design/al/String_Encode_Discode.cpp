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

int main() {
    // 1. Empty string
    assert(encode("") == "");
    assert(decode("") == "");

    // 2. Single character
    assert(encode("a") == "a1");
    assert(decode("a1") == "a");

    // 3. One run
    assert(encode("aaaa") == "a4");
    assert(decode("a4") == "aaaa");

    // 4. Multiple runs
    assert(encode("aaabbbb") == "a3b4");
    assert(decode("a3b4") == "aaabbbb");

    // 5. Alternating characters
    assert(encode("ababab") == "a1b1a1b1a1b1");
    assert(decode("a1b1a1b1a1b1") == "ababab");

    // 6. Multi-digit count
    assert(encode("aaaaaaaaaaaa") == "a12");
    assert(decode("a12") == "aaaaaaaaaaaa");

    // 7. Mixed runs
    assert(encode("aabccccdde") == "a2b1c4d2e1");
    assert(decode("a2b1c4d2e1") == "aabccccdde");

    // 8. Decode with large count
    assert(decode("z100").size() == 100);

    // 9. Encode -> Decode
    string s1 = "aaabbbccccccddddde";
    assert(decode(encode(s1)) == s1);

    // 10. Decode -> Encode
    string s2 = "a10b20c1";
    assert(encode(decode(s2)) == s2);

    cout << "All tests passed!" << endl;
}
