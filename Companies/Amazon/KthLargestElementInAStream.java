package Companies.Amazon;

import java.util.PriorityQueue;

public class KthLargestElementInAStream {
    private PriorityQueue<Integer> q;
    private int k;

    public KthLargestElementInAStream(int k, int[] nums) {
        q = new PriorityQueue<>(k);
        this.k = k;
        for (int num : nums) {
            add(num); // add not q.add;
        }

//        minHeap = new int[k];
//        for(int i = 0; i < k; i++){
//            if(i < nums.length){
//                minHeap[i] = nums[i];
//            }else{
//                minHeap[i] = Integer.MIN_VALUE;
//            }
//        }
//
//        //    0
//        //  1   2
//        // 3 4 5 6
//        for(int i = (int)Math.floor(k/2); i >= 0; i--){
//            minHeapify(minHeap, i);
//        }
//        for(int i = k; i < nums.length; i++){
//            if(nums[i] > minHeap[0]){
//                minHeap[0] = nums[i];
//                minHeapify(minHeap, 0);
//            }
//        }
    }

    public int add(int val) {
        if (q.size() < k) {
            q.add(val);
        } else if (val > q.peek()) {
            q.poll();
            q.add(val);
        }
        return q.peek();
    }

//    private int[] minHeap;

//    public int add(int val) {
//        if(val > minHeap[0]){
//            minHeap[0] = val;
//            minHeapify(minHeap, 0);
//        }
//        return minHeap[0];
//    }
//
//    private void minHeapify(int[] heap, int i){
//        int l = 2*i+1;
//        int r = 2*i+2;
//
//        int small = i;
//        if(l < heap.length && heap[i] > heap[l]){
//            small = l;
//        }
//        if(r < heap.length && heap[small] > heap[r]){
//            small = r;
//        }
//        if(small != i){
//            int temp = heap[i];
//            heap[i] = heap[small];
//            heap[small] = temp;
//
//            minHeapify(minHeap, small);
//        }
//    }
}
