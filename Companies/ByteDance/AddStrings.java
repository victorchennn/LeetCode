package Companies.ByteDance;

public class AddStrings {
    public String addStrings(String num1, String num2) {
        StringBuilder sb = new StringBuilder();
        int carry = 0;
        for (int i = num1.length()-1, j = num2.length()-1; i >= 0 || j>= 0 || carry != 0; i--, j--) {
            int x = i < 0?0:num1.charAt(i)-'0';
            int y = j < 0?0:num2.charAt(j)-'0';
            int sum = x + y + carry;
            sb.append(sum%10);
            carry = sum/10;
        }
        return sb.reverse().toString();
    }
}
