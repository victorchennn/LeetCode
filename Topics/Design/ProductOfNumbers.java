package Topics.Design;

import java.util.LinkedList;
import java.util.List;

public class ProductOfNumbers {
    private List<Integer> l;

    public ProductOfNumbers() {
        add(0);
    }

    public void add(int num) {
        if (num > 0) {
            l.add(l.get(l.size()-1)*num);
        } else {
            l = new LinkedList<>();
            l.add(1);
        }
    }

    public int getProduct(int k) {
        int n = l.size();
        return k<n? l.get(n-1)/l.get(n-k-1):0;
    }
}
