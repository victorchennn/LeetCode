package Topics.Design;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FileSystem {

    private FileNode root;

    public FileSystem() {
        root = new FileNode();
    }

    /**
     * If it is a file path, return a list that only contains this file's name.
     * If it is a directory path, return the list of file and directory names in this directory.
     * Output (file and directory names together) should in lexicographic order.
     */
    public List<String> ls(String path) {
        FileNode cur = root;
        String[] paths = path.split("/");
        for (int i = 1; i < paths.length; i++) {
            cur = cur.children.get(paths[i]);
        }
        List<String> re = new ArrayList<>();
        if (cur.fileName != null) {
            re.add(cur.fileName);
        } else {
            re.addAll(cur.children.keySet());
        }
        return re;
    }

    public void mkdir(String path) {
        FileNode cur = root;
        String[] paths = path.split("/");
        for (int i = 1; i < paths.length; i++) {
            cur.children.putIfAbsent(paths[i], new FileNode());
            cur = cur.children.get(paths[i]);
        }
    }

    public void addContentToFile(String filePath, String content) {
        FileNode cur = root;
        String[] paths = filePath.split("/");
        for (int i = 1; i < paths.length; i++) {
            if (cur.children.containsKey(paths[i])) {
                cur = cur.children.get(paths[i]);
            }
        }
        if (cur.fileName == null) {
            cur.children.put(paths[paths.length-1], new FileNode());
            cur = cur.children.get(paths[paths.length-1]);
            cur.fileName = paths[paths.length-1];
            cur.content = "";
        }
        cur.content += content;
    }

    public String readContentFromFile(String filePath) {
        FileNode cur = root;
        String[] paths = filePath.split("/");
        for (int i = 1; i < paths.length; i++) {
            cur = cur.children.get(paths[i]);
        }
        return cur.content;
    }


    class FileNode {
        TreeMap<String, FileNode> children = new TreeMap<>();
        String fileName, content;
    }
}
