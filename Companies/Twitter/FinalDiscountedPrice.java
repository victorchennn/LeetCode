package Companies.Twitter;

import java.util.Stack;

public class FinalDiscountedPrice {

    private int getTotalCost(int[] prices) {
        int[] tmp = new int[prices.length];
        for(int i=0;i<tmp.length;i++) {
            tmp[i] = prices[i];
        }
        Stack<Integer> s = new Stack<>();
        for(int i=0;i<prices.length;i++) {
            while(!s.isEmpty() && prices[s.peek()] >= prices[i]) {
                int pre = s.pop();
                tmp[pre] = prices[pre] - prices[i];
            }
            s.push(i);
        }
        int res = 0;
        for(int t : tmp)
            res += t;
        return res;
    }

    public static void main(String[] args) {
        int[] prices = {2,3,1,2,4,2};
        FinalDiscountedPrice test = new FinalDiscountedPrice();
        System.out.println(test.getTotalCost(prices));
    }
}
