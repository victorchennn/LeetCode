package Companies.Microsoft;

public class ExcelSheetColumnTitle {
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
