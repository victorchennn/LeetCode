// Given a string s, return the maximum number of occurrences of any substring under the following rules:

// The number of unique characters in the substring must be less than or equal to maxLetters.
// The substring size must be between minSize and maxSize inclusive.

class Solution {
public:
    int maxFreq(const std::string& s, int maxLetters, int minSize, int maxSize) {
        int maxFrequency = 0;
        std::unordered_map<std::string, int> frequency;

        for (int start = 0; start + minSize <= static_cast<int>(s.size()); ++start) {
            std::string substring = s.substr(start, minSize);
            std::unordered_set<char> uniqueCharacters;
            for (char c : substring) {
                uniqueCharacters.insert(c);
            }

            if (static_cast<int>(uniqueCharacters.size()) <= maxLetters) {
                int currentFrequency = ++frequency[substring];
                maxFrequency = std::max(maxFrequency, currentFrequency);
            }
        }

        return maxFrequency;
    }
};
