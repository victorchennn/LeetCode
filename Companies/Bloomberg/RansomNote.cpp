    public boolean canConstruct(String ransomNote, String magazine) {
        int[] letterFrequency = new int[26];
      
        for (char ch : magazine.toCharArray()) {
            int charIndex = ch - 'a';
            letterFrequency[charIndex]++;
        }
      
        for (char ch : ransomNote.toCharArray()) {
            int charIndex = ch - 'a';
            letterFrequency[charIndex]--;
            if (letterFrequency[charIndex] < 0) {
                return false;
            }
        }
      
        return true;
    }
