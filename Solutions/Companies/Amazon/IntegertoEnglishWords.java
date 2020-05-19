package Companies.Amazon;

public class IntegertoEnglishWords {
    private String[] A = {"", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine"};
    private String[] B = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen"};
    private String[] C = {"", "Ten", "Twenty", "Thirty", "Forty", "Fifty", "Sixty", "Seventy", "Eighty", "Ninety"};

    public String numberToWords(int num) {
        if (num == 0) {
            return "Zero";
        }
        return dfs(num);
    }

    private String dfs(int num) {
        String re = "";
        if (num < 10) {
            re =  A[num];
        } else if (num < 20) {
            re =  B[num-10];
        } else if (num < 100) {
            re = C[num/10] + " " + A[num%10];
        } else if (num < 1000) {
            re = dfs(num/100) + " Hundred " + dfs(num%100);
        } else if (num < 1000000) {
            re = dfs(num/1000) + " Thousand " + dfs(num%1000);
        } else if (num < 1000000000) {
            re = dfs(num/1000000) + " Million " + dfs(num%1000000);
        } else {
            re = dfs(num/1000000000) + " Billion " + dfs(num%1000000000);
        }
        return re.trim();
    }
}
