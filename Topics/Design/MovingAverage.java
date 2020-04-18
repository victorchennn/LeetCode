package Topics.Design;

public class MovingAverage {
    int[] nums;
    double sum = 0;
    int count = 0, p = 0;

    /** Initialize your data structure here. */
    public MovingAverage(int size) {
        nums = new int[size];
    }

    public double next(int val) {
        sum -= nums[p];
        sum += val;
        nums[p] = val;
        if (count < nums.length) {
            count++;
        }
        p = (p+1)%nums.length;
        return sum/count;
    }
}
