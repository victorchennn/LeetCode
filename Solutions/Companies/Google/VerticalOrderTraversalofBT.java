package Companies.Google;

import Libs.TreeNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerticalOrderTraversalofBT {

    List<Location> l;

    public List<List<Integer>> verticalTraversal(TreeNode root) {
        List<List<Integer>> re = new ArrayList<>();
        if (root == null) {
            return re;
        }
        l = new ArrayList<>();
        dfs(root, 0, 0);
        Collections.sort(l);
        re.add(new ArrayList<>());
        int col = l.get(0).x;
        for (Location lo : l) {
            if (lo.x != col) {
                re.add(new ArrayList<>());
                col = lo.x;
            }
            re.get(re.size()-1).add(lo.val);
        }
        return re;
    }

    private void dfs(TreeNode root, int x, int y) {
        if (root != null) {
            l.add(new Location(x, y, root.val));
            dfs(root.left, x-1, y+1);
            dfs(root.right, x+1, y+1);
        }
    }

    class Location implements Comparable<Location> {
        int x, y, val;

        Location(int _x, int _y, int _val) {
            x = _x;
            y = _y;
            val = _val;
        }

        @Override
        public int compareTo(Location l) {
            if (this.x != l.x) {
                return Integer.compare(this.x, l.x);
            } else if (this.y != l.y) {
                return Integer.compare(this.y, l.y);
            } else {
                return Integer.compare(this.val, l.val);
            }
        }
    }
}
