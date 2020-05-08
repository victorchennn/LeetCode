package Companies.Amazon;

//    a = 0011 1100
//    b = 0000 1101
//  -----------------
//  a&b = 0000 1100 bitwise and
//  a|b = 0011 1101 bitwise or
//  a^b = 0011 0001 bitwise XOR
//  ~a  = 1100 0011 bitwise compliment

import java.util.HashSet;
import java.util.Set;

public class SingleNumber {
    public int singleNumber(int[] nums) {
        int re = 0;
        for (int i : nums) {
            re ^= i;
        }
        return re;
    }

    public int singleNumber_2(int[] nums) {
        Set<Integer> s = new HashSet<>();
        int sum = 0, dsum = 0;
        for (int num : nums) {
            if (!s.contains(num)) {
                s.add(num);
                sum += num;
            }
            dsum += num;
        }
        return sum*2-dsum;
    }

    /* Every element appears three times except for one */
    public int singleNumberII(int[] nums) {
        int one = 0, two = 0;
        for (int i : nums) {
            one = ~two & (one ^ i);
            two = ~one & (two ^ i);
        }
        return one;
    }

    /* exactly two elements appear only once and all the other elements appear exactly twice */
    public int[] singleNumberIII(int[] nums) {
        int i = 0;
        for (int num : nums) {
            i ^= num;
        }
        int diff = i & (-i);
        int x = 0;
        for (int num : nums) {
            if ((num & diff) != 0) {
                x ^= num;
            }
        }
        return new int[]{x, i^x};
    }
}
