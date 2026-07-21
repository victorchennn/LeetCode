

// Given a string s, return the maximum number of occurrences of any substring under the following rules:

// The number of unique characters in the substring must be less than or equal to maxLetters.
// The substring size must be between minSize and maxSize inclusive.

public int maxFreq(String s, int maxLetters, int minSize, int maxSize) {
        int maxFrequency = 0;
        // Map to store substring frequency count
        Map<String, Integer> substringFrequencyMap = new HashMap<>();
      
        // Iterate through all possible substrings of minSize length
        for (int startIndex = 0; startIndex <= s.length() - minSize; startIndex++) {
            // Extract substring of minSize length starting at startIndex
            String currentSubstring = s.substring(startIndex, startIndex + minSize);
          
            // Count unique characters in the current substring
            Set<Character> uniqueCharacters = new HashSet<>();
            for (int j = 0; j < minSize; j++) {
                uniqueCharacters.add(currentSubstring.charAt(j));
            }
          
            // Check if the number of unique characters is within the limit
            if (uniqueCharacters.size() <= maxLetters) {
                // Update frequency count for this valid substring
                int currentFrequency = substringFrequencyMap.getOrDefault(currentSubstring, 0) + 1;
                substringFrequencyMap.put(currentSubstring, currentFrequency);
              
                // Update the maximum frequency found so far
                maxFrequency = Math.max(maxFrequency, currentFrequency);
            }
        }
      
        return maxFrequency;
    }
