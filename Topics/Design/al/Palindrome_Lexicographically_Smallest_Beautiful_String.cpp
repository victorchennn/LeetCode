// A string is beautiful if:
// It consists of the first k letters of the English lowercase alphabet.
// It does not contain any substring of length 2 or more which is a palindrome.

// You are given a beautiful string s of length n and a positive integer k.
// Return the lexicographically smallest string of length n, which is larger than s and is beautiful. If there is no such string, return an empty string.

// Input: s = "abcz", k = 26
// Output: "abda"

class Solution {
public:
    string smallestBeautifulString(string s, int k) {
        int n = s.size();

        for (int i = n - 1; i >= 0; --i) {
            for (char c = s[i] + 1; c < 'a' + k; ++c) {
                if (!valid(s, i, c)) {
                    continue;
                }

                s[i] = c;

                for (int j = i + 1; j < n; ++j) {
                    for (char next = 'a'; next < 'a' + k; ++next) {
                        if (valid(s, j, next)) {
                            s[j] = next;
                            break;
                        }
                    }
                }

                return s;
            }
        }

        return "";
    }

private:
    bool valid(const string& s, int i, char c) {
        return (i < 1 || s[i - 1] != c) &&
               (i < 2 || s[i - 2] != c);
    }
};
