package Companies.Google;

public class MultiplyStrings {
    public String multiply(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        int[] dp = new int[m+n];
        for (int i = m-1; i >= 0; i--) {
            for (int j = n-1; j >= 0; j--) {
                int num = (num1.charAt(i)-'0')*(num2.charAt(j)-'0');
                int p1 = i+j, p2 = i+j+1;
                int sum = num + dp[p2]; // !!!!
                dp[p2] = sum%10;
                dp[p1] += sum/10;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i : dp) {
            if (!(sb.length() == 0 && i == 0)) { // !!!!
                sb.append(i);
            }
        }
        return sb.length() == 0? "0":sb.toString();
    }
}
