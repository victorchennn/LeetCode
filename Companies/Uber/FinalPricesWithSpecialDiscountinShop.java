package Companies.Uber;

import java.util.Stack;

/**
 * if you buy the ith item, then you will receive a discount equivalent to prices[j]
 * where j is the minimum index such that j > i and prices[j] <= prices[i]
 *
 * @see Companies.Bloomberg.NextGreaterElement
 */
public class FinalPricesWithSpecialDiscountinShop {
    public int[] finalPrices(int[] prices) {
        Stack<Integer> s = new Stack<>();
        for (int i = 0; i < prices.length; i++) {
            while (!s.empty() && prices[s.peek()] >= prices[i]) {
                prices[s.pop()] -= prices[i];
            }
            s.push(i);
        }
        return prices;
    }
}
