package Companies.Facebook;

public class ValidateIPAddress {
    public String validIPAddress(String IP) {
        String[] ipv4 = IP.split("\\.", -1);
        String[] ipv6 = IP.split("\\:", -1);
        if (IP.chars().filter(c -> c == '.').count() == 3) {
            for (String s : ipv4) {
                if (!isIPv4(s)) {
                    return "Neither";
                }
            }
            return "IPv4";
        }
        if (IP.chars().filter(c -> c == ':').count() == 7) {
            for (String s : ipv6) {
                if (!isIPv6(s)) {
                    return "Neither";
                }
            }
            return "IPv6";
        }
        return "Neither";
    }

    private boolean isIPv4(String s) {
        try {
            int num = Integer.valueOf(s);
            return String.valueOf(num).equals(s) && num >= 0 && num <= 255;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private boolean isIPv6(String s) {
        try {
            return s.length() <= 4 && Integer.parseInt(s, 16) >= 0 && s.charAt(0) != '-';
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
