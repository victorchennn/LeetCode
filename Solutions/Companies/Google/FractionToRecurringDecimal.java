package Companies.Google;

import java.util.HashMap;
import java.util.Map;

public class FractionToRecurringDecimal {
    public String fractionToDecimal(int numerator, int denominator) {
        if (numerator == 0) {
            return "0";
        }
        StringBuilder sb = new StringBuilder();
        if (numerator > 0 && denominator < 0 || numerator < 0 && denominator > 0) {
            sb.append("-");
        }
        long divisor = Math.abs((long)numerator);
        long dividend = Math.abs((long)denominator);
        sb.append(divisor/dividend);
        long re = divisor%dividend;
        if (re == 0) {
            return sb.toString();
        }
        sb.append(".");
        Map<Long, Integer> m = new HashMap<>();
        while (re > 0) {
            if (m.containsKey(re)) {
                sb.insert(m.get(re), "(");
                sb.append(")");
                break;
            }
            m.put(re, sb.length());
            re *= 10;
            sb.append(re/dividend);
            re %= dividend;
        }
        return sb.toString();
    }
}
