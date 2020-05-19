package Companies.Google;

import java.util.HashSet;
import java.util.Set;

public class UniqueEmailAddresses {
    public int numUniqueEmails(String[] emails) {
        Set<String> s = new HashSet<>();
        for (String e : emails) {
            String local = e.substring(0, e.indexOf("@"));
            String domain = e.substring(e.indexOf("@"));
            if (local.contains("+")) {
                local = local.substring(0, local.indexOf("+"));
            }
            local = local.replaceAll("\\.", "");
            s.add(local+domain);
        }
        return s.size();
    }

    public static void main(String...args) {
        UniqueEmailAddresses a = new UniqueEmailAddresses();
        a.numUniqueEmails(new String[]{"test.email+alex@leetcode.com","test.email.leet+alex@code.com"});
    }
}
