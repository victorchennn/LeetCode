package Companies.Bloomberg;

import java.util.*;

public class ReorderBasedOnTitles {
    private static Employee[] sortArray(Employee[] employees, Map<String, String> order) {
        Set<String> roles = new HashSet<>();
        roles.addAll(order.keySet());
        roles.addAll(order.values());

        Map<String, Integer> inDegrees = new HashMap<>();
        Map<String, Set<String>> sat = new HashMap<>();
        for (String role : roles) {
            inDegrees.put(role, 0);
        }
        for (String before : order.keySet()) {
            inDegrees.put(before, inDegrees.getOrDefault(order.get(before), 0)+1);
            sat.computeIfAbsent(order.get(before), k->new HashSet<>()).add(before);
        }
        Queue<String> q = new LinkedList<>();
        List<String> lst = new ArrayList<>();
        for (String title : inDegrees.keySet()) {
            if (inDegrees.get(title) == 0) {
                q.add(title);
                lst.add(title);
            }
        }
        while (!q.isEmpty()) {
            String cur = q.poll();
            for (String nei : sat.getOrDefault(cur, new HashSet<>())) {
                inDegrees.put(nei, inDegrees.get(nei) - 1);
                if (inDegrees.get(nei) == 0) {
                    lst.add(nei);
                    q.offer(nei);
                }
            }
        }
        Map<String, Integer> levelMap = new HashMap<>();
        for (int i = 0; i < lst.size(); i++) {
            levelMap.put(lst.get(i), i);
        }
        Arrays.sort(employees, (a, b) -> levelMap.get(b.role) - levelMap.get(a.role));
        return employees;
    }

    public static void main(String[] args) {
        Map<String, String> map = new HashMap<String, String>() {{
            put("CTO", "CEO");
            put("Manager", "CTO");
            put("Engineer", "Manager");
            put("CFO", "CEO");
        }};
        Employee[] employees = new Employee[6];

        employees[0] = new Employee("John", "Manager");
        employees[1] = new Employee("Sally", "CTO");
        employees[2] = new Employee("Sam", "CEO");
        employees[3] = new Employee("Drax", "Engineer");
        employees[4] = new Employee("Bob", "CFO");
        employees[5] = new Employee("Daniel", "Engineer");
        System.out.println(Arrays.toString(sortArray(employees, map)));
    }

    static class Employee {
        String name;
        String role;

        public Employee(String name, String role) {
            this.name = name;
            this.role = role;
        }

        @Override
        public String toString() {
            return "Employee [name=" + name + ", role=" + role + "]";
        }
    }
}
