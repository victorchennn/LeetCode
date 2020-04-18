package Companies.Microsoft;

public class RomantoInteger {
    public int romanToInt(String s) {
        int re = 0;
        for (int i = s.length()-1; i >= 0; i--) {
            char c  = s.charAt(i);
            switch(c) {
                case 'I':
                    re += (re >= 5?-1:1);
                    break;
                case 'V':
                    re += 5;
                    break;
                case 'X':
                    re += 10*(re >= 50?-1:1);
                    break;
                case 'L':
                    re += 50;
                    break;
                case 'C':
                    re += 100*(re >= 500?-1:1);
                    break;
                case 'D':
                    re += 500;
                    break;
                case 'M':
                    re += 1000;
                    break;
            }
        }
        return re;
    }
}
