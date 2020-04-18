package Companies.Bloomberg;

public class PlusOne {
    public int[] plusOne(int[] digits) {
        int i = digits.length-1;
        while (i >= 0 && digits[i] == 9) {
            i--;
        }
        if (i >= 0) {
            digits[i] += 1;
            for (int j = i+1; j < digits.length; j++) {
                digits[j] = 0;
            }
            return digits;
        } else {
            int[] re = new int[digits.length+1];
            re[0] = 1;
            return re;
        }
    }
}
