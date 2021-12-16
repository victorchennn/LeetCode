package Companies.Amazon;

import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AsteroidCollision {
    public int[] asteroidCollision(int[] asteroids) {
        Deque<Integer> stack = new ArrayDeque<>();
        for (int as : asteroids) {
            int add = as;
            while (!stack.isEmpty() && stack.peek() > 0 && as < 0) {
                if (stack.peek() > Math.abs(as)) {
                    add = 0;
                    break;
                } else if (stack.peek() == Math.abs(as)) {
                    add = 0;
                    stack.pop();
                    break;
                } else {
                    stack.pop();
                }
            }
            if (add != 0) {
                stack.push(add);
            }
        }
        int[] re = new int[stack.size()];
        for (int i = stack.size()-1; i >= 0; i--) {
            re[i] = stack.pop();
        }
        return re;

//        for (int ast: asteroids) {
//            collision: {
//                while (!stack.isEmpty() && ast < 0 && 0 < stack.peek()) {
//                    if (stack.peek() < -ast) {
//                        stack.pop();
//                        continue;
//                    } else if (stack.peek() == -ast) {
//                        stack.pop();
//                    }
//                    break collision;
//                }
//                stack.push(ast);
//            }
//        }
    }

    @Test
    void test() {
        assertEquals(true, Arrays.equals(asteroidCollision(new int[]{5,10,-5}), new int[]{5,10}));
        assertEquals(true, Arrays.equals(asteroidCollision(new int[]{8,-8}), new int[]{}));
        assertEquals(true, Arrays.equals(asteroidCollision(new int[]{10,2,-5}), new int[]{10}));
        assertEquals(true, Arrays.equals(asteroidCollision(new int[]{-2,-1,1,2}), new int[]{-2,-1,1,2}));
    }
}
