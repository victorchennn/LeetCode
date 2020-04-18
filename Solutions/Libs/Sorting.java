package Libs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Multiple sorting algorithms for practicing.
 * @author Victor
 */

public class Sorting {

    /**
     * Good pivot -> median, bad pivot -> largest/smallest,
     * average O(nlog n), worst O(n^2).
     * Space complexity is O(log n).
     */
    private void quickSort(int[] a){
        help_quicksort(a, 0, a.length - 1);
    }

    /**
     * Divide array to two equal parts, need two pointers and
     * two temp arrays for merging, O(NlogN).
     * Space complexity is O(log n).
     */
    private void mergeSort(int[] a) {
        help_mergesort(a, 0, a.length - 1);
    }

    /**
     * Best O(n) when the list is already sorted. Average O(n^2)
     * Space complexity is O(1).
     */
    private void bubbleSort(int[] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length-i-1; j++) {
                if (a[j] > a[j+1]) {
                    swap(a, j, j+1);
                }
            }
        }
    }

    /**
     * LSD(least significant sort), right to left.
     */
    private void radixsort(int[] a) {
        List<Integer>[] buckets = new ArrayList[10];
        for (int i = 0; i < 10; i++) {
            buckets[i] = new ArrayList<>();
        }
        boolean flag = false;
        int divisor = 1;
        while (!flag) {
            flag = true;
            for (Integer i : a) {
                int digit = i / divisor;
                buckets[digit % 10].add(i);
                if (flag && digit > 0) {
                    flag = false;
                }
            }
            int p = 0;
            for (int index = 0; index < 10; index++) {
                for (Integer i : buckets[index]) {
                    a[p++] = i;
                }
                buckets[index].clear();
            }
            divisor *= 10;
        }
    }

    private void help_mergesort(int[] a, int low, int high) {
        if (low < high) {
            int m = (low + high) / 2;
            help_mergesort(a, low, m);
            help_mergesort(a, m+1, high);
            merge(a, low, m, high);
        }
    }

    private void merge(int[] a, int l, int m, int h) {
        int leftLen = m-l+1;
        int rightLen = h-m;
        int[] left = new int[leftLen];
        int[] right = new int[rightLen];
        for (int i = 0; i < leftLen; i++) {
            left[i] = a[l+i];
        }
        for (int i = 0; i < rightLen; i++) {
            right[i] = a[m+1+i];
        }
        int i = 0, j = 0;
        while (i < leftLen && j < rightLen) {
            if (left[i] < right[j]) {
                a[l+i+j] = left[i];
                i++;
            } else {
                a[l+i+j] = right[j];
                j++;
            }
        }
        while (i < leftLen) {
            a[l+i+j] = left[i];
            i++;
        }
        while (j < rightLen) {
            a[l+i+j] = right[j];
            j++;
        }
    }

    private int[] help_quicksort(int[] nums, int low, int high) {
        int l = low, r = high, p = nums[low];
        while (l < r) {
            while (l < r && nums[r] >= p) {
                r--;
            }
            nums[l] = nums[r];
            while (l < r && nums[l] <= p) {
                l++;
            }
            nums[r] = nums[l];
        }
        nums[l] = p;
        if (l > low) {
            help_quicksort(nums, low, l-1);
        }
        if (r < high) {
            help_quicksort(nums, r+1, high);
        }
        return nums;
    }

    private void swap(int[] a, int n, int m) {
        int temp = a[n];
        a[n] = a[m];
        a[m] = temp;
    }

    public static void main(String...args) {
        int[] a1 = new int[]{7,11,12,9,10,3,6,0,5,2,4,8,1,13};
        int[] a2 = new int[]{12,6,8,13,1,5,0,9,4,11,10,7,2,3};
        int[] a3 = new int[]{852,142,284,923,355,00,
                71,497,639,710,426,781,213};
//        System.arraycopy();

        Sorting test = new Sorting();
        System.out.println(Arrays.toString(a1));
        test.quickSort(a1);
        System.out.println(Arrays.toString(a1));

        System.out.println(Arrays.toString(a2));
        test.mergeSort(a2);
        System.out.println(Arrays.toString(a2));

        System.out.println(Arrays.toString(a3));
        test.radixsort(a3);
        System.out.println(Arrays.toString(a3));

        System.out.println(Arrays.toString(a3));
        test.bubbleSort(a3);
        System.out.println(Arrays.toString(a3));
    }
}
