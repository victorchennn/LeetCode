package Companies.Bloomberg;

import java.util.*;

public class InvalidTransactions {
    public List<String> invalidTransactions(String[] transactions) {
        Map<String, Set<Transaction>> m = new HashMap<>();
        for (String str : transactions) {
            Transaction t = new Transaction(str);
            m.computeIfAbsent(t.name, k->new HashSet<>()).add(t);
        }

        Set<String> unique = new HashSet<>();
        for (String name : m.keySet()) {
            Set<Transaction> ts = m.get(name);
            for (Transaction t : ts) {
                if (unique.contains(t.str)) {
                    continue;
                }
                if (t.amount > 1000) {
                    unique.add(t.str);
                    continue;
                }
                for (Transaction ot : ts) {
                    if (!ot.city.equals(t.city) && Math.abs(ot.time - t.time) <= 60) {
                        unique.add(t.str);
                        unique.add(ot.str);
                        break;
                    }
                }
            }
        }
        return new ArrayList<>(unique);
    }

    class Transaction{
        String name, city, str;
        int time, amount;
        Transaction(String transaction) {
            str = transaction;
            String[] ts = transaction.split(",");
            name = ts[0];
            time = Integer.valueOf(ts[1]);
            amount = Integer.valueOf(ts[2]);
            city = ts[3];
        }
    }

//    public List<String> invalidTransactions(String[] transactions) {
//        Map<String, List<String[]>> m = new HashMap<>();
//        for (String t : transactions) {
//            String[] ss = t.split(",");
//            m.computeIfAbsent(ss[0], k->new ArrayList<>()).add(ss);
//        }
//        List<String> re = new ArrayList<>();
//        for (String t : transactions) {
//            String[] ss = t.split(",");
//            if (Integer.valueOf(ss[2]) > 1000) {
//                re.add(t);
//            } else {
//                for (String[] other : m.get(ss[0])) {
//                    if (Math.abs(Integer.valueOf(other[1])-Integer.valueOf(ss[1])) <= 60 && !other[3].equals(ss[3])) {
//                        re.add(t);
//                        break;
//                    }
//                }
//            }
//        }
//        return re;
//    }
}
