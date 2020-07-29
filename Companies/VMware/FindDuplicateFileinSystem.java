package Companies.VMware;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDuplicateFileinSystem {
    public List<List<String>> findDuplicate(String[] paths) {
        Map<String, List<String>> m = new HashMap<>();
        for (String path : paths) {
            String[] files = path.split(" ");
            for (int i = 1; i < files.length; i++) {
                String[] name_content = files[i].split("\\(");
                String name = name_content[0];
                String content = name_content[1].replace(")", "");
                List<String> l = m.getOrDefault(content, new ArrayList<>());
                l.add(files[0] + "/" + name);
                m.put(content, l);
            }
        }
        List<List<String>> re = new ArrayList<>();
        for (String k : m.keySet()) {
            if (m.get(k).size() > 1) {
                re.add(m.get(k));
            }
        }
        return re;
    }
}
