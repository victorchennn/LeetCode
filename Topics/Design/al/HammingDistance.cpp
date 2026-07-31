class Solution {
public:
    /**
     * Calculate the Hamming distance between two integers.
     * The Hamming distance is the number of positions at which 
     * the corresponding bits are different.
     * 
     * @param x First integer
     * @param y Second integer
     * @return The number of bit positions where x and y differ
     */
    int hammingDistance(int x, int y) {
        // XOR operation highlights the differing bits (sets them to 1)
        // __builtin_popcount counts the number of set bits (1s)
        return __builtin_popcount(x ^ y);
        // return __builtin_popcount(~(x ^ y)); 
    }

    int hammingDistance(int x, int y) {
        int diff = x ^ y;
        int count = 0;
    
        while (diff) {
            diff &= (diff - 1);
            ++count;
        }
    
        return count;
    }
    
};
