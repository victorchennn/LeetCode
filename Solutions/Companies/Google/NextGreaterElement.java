package Companies.Google;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * @see Companies.Bloomberg.LastGreaterElement
 */
public class NextGreaterElement {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer, Integer> m = new HashMap<>();
        Stack<Integer> s = new Stack<>();
        for (int num : nums2) {
            while (!s.isEmpty() && s.peek() < num) {
                m.put(s.pop(), num);
            }
            s.push(num);
        }
        int[] re = new int[nums1.length];
        for (int i = 0; i < nums1.length; i++) {
            re[i] = m.getOrDefault(nums1[i], -1);
        }
        return re;
    }

    /**
     * Circular array.
     * Input: [1,2,1]
     * Output: [2,-1,2]
     */
    public int[] nextGreaterElements(int[] nums) {
        int len = nums.length;
        int[] re = new int[len];
        Arrays.fill(re, -1);
        Stack<Integer> s = new Stack<>();
        for (int i = 0; i < len*2; i++) {
            int num = nums[i%len];
            while (!s.isEmpty() && nums[s.peek()] < num) {
                re[s.pop()] = num;
            }
            if (i < len) {
                s.push(i);
            }
        }
        return re;
    }

    /**
     * Input:  5349876
     * Output: 5364789
     * @see NextPermutation
     */
    public int nextGreaterElement(int n) {
        char[] num = String.valueOf(n).toCharArray();
        int i = num.length-2;
        while (i >= 0 && num[i] >= num[i+1]) {
            i--;
        }
        if (i == -1) {
            return -1;
        }
        int j = num.length-1;
        while (num[j] <= num[i]) {
            j--;
        }
        swap(num, i, j);
        reverse(num, i+1, num.length-1);
        try {
            return Integer.valueOf(String.valueOf(num));
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void swap(char[] num, int i, int j) {
        char temp = num[j];
        num[j] = num[i];
        num[i] = temp;
    }

    private void reverse(char[] num, int i, int j) {
        while (i < j) {
            swap(num, i, j);
            i++;
            j--;
        }
    }
}
