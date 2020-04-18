package Companies.Bloomberg;

import java.io.File;
import java.util.List;

public class MergeFile {
    public File merge(List<File> files) {
        if (files.size() == 0) {
            return null;
        }
        return helpMerge(files, 0, files.size()-1);
    }

    private File helpMerge(List<File> files, int l, int r) {
        if (l == r) {
            return files.get(l);
        }
        int m = l + (r-l)/2;
        File left = helpMerge(files, l, m);
        File right = helpMerge(files, m+1, r);
        return toMerge(left, right);
    }

    private File toMerge(File f1, File f2) {
        File re = new File();
        while (f1.hasNext() && f2.hasNext()) {
            String l1 = f1.readLine();
            String l2 = f2.readLine();
            re.writeLine(l1);
            re.writeLine(l2);
        }
        while (f1.hasNext()) {
            re.writeLine(f1.readLine());
        }
        while (f2.hasNext()) {
            re.writeLine(f2.readLine());
        }
        return re;
    }

    class File{

        File() {}

        String readLine() {
            return "";
        }

        boolean hasNext() {
            return true;
        }

        void writeLine(String s) {

        }
    }
}
