package Companies.Tencent;

import java.util.HashMap;
import java.util.Map;

public class FractiontoRecurringDecimal {
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        if (numerator > 0 && denominator < 0 || numerator < 0 && denominator > 0) {
            sb.append('-');
        }
        long num = Math.abs((long)numerator);
        long deno = Math.abs((long)denominator);
        sb.append(num/deno);
        long re = num%deno;
        if (re == 0) {
            return sb.toString();
        }
        sb.append('.');
        Map<Long, Integer> m = new HashMap<>();
        while (re > 0) {
            if (m.containsKey(re)) {
                sb.insert(m.get(re), "("); // need to be "(";
                sb.append(")");
                break;
            }
            m.put(re, sb.length());
            re *= 10;
            sb.append(re/deno);
            re %= deno;
        }
        return sb.toString();
    }
}
