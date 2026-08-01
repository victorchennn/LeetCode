// Input: n = 1000  Output: 1333
// 
// 1333 is numerically balanced since:
// - The digit 1 occurs 1 time.
// - The digit 3 occurs 3 times. 
// It is also the smallest numerically balanced number strictly greater than 1000.
// Note that 1022 cannot be the answer because 0 appeared more than 0 times.
// 
// Input: n = 3000 Output: 3133
// 
// 3133 is numerically balanced since:
// - The digit 1 occurs 1 time.
// - The digit 3 occurs 3 times.
// It is also the smallest numerically balanced number strictly greater than 3000.

class Solution {
public:
    int nextBeautifulNumber(int n) {
        // Iterate through numbers starting from n + 1 until we find a beautiful number
        for (int candidate = n + 1;; ++candidate) {
            // Count frequency of each digit (0-9) in the current number
            int digitFrequency[10] = {0};
          
            // Extract each digit and count its frequency
            for (int temp = candidate; temp > 0; temp /= 10) {
                int digit = temp % 10;
                ++digitFrequency[digit];
            }
          
            // Check if the current number is beautiful
            // A beautiful number has each digit appearing exactly as many times as its value
            bool isBeautiful = true;
          
            // Verify each digit appears exactly as many times as its value
            for (int temp = candidate; temp > 0; temp /= 10) {
                int digit = temp % 10;
              
                // If digit doesn't appear exactly 'digit' times, it's not beautiful
                if (digit != digitFrequency[digit]) {
                    isBeautiful = false;
                    break;
                }
            }
          
            // Return the first beautiful number found
            if (isBeautiful) {
                return candidate;
            }
        }
    }
};
