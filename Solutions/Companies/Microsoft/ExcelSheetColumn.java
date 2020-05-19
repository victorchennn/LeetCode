package Companies.Microsoft;

public class ExcelSheetColumn {
    public int titleToNumber(String s) {
        int num = 0;
        for (int i = 0; i < s.length(); i++) {
            num = num*26 + s.charAt(i)-'A'+1;
        }
        return num;
    }

    public String convertToTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while (n > 0) {
            n--;
            sb.insert(0, (char)(n%26+'A'));
            n /= 26;
        }
        return sb.toString();
    }
}
