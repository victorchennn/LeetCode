package Companies.Bloomberg;

/**
 * Two files, each of 100TB in size, each file composed of trades that take up
 * one line each, each trade has a unique key.
 *
 * You have 1 megabyte of RAM, print out the lines to an output file that are in
 * file a but not file b as well as the lines that are in file b but not in file a.
 *
 * Sort file A and B using external sort(merge sort) write it back to the disk.
 * Merge sort algorithm, which sorts chunks that each fit in RAM, then merges the sorted
 * chunks together. We first divide the file into runs such that the size of a run is
 * small enough to fit into main memory. Then sort each run in main memory using merge sort.
 * Finally merge the resulting runs together into successively bigger runs, until the file is sorted.
 *
 * MergeSort : Used for sort individual runs (a run is part of file that is small enough to fit in main memory)
 * Merge K Sorted Arrays : Used to merge sorted runs.
 *
 * Retrieve first block of A and B where size(blockA) + size(blockB) < 1MB.
 * Run two pointers on A and B respectively. Compare A against B. If a matching value in B isn't
 * found then output it to screen / store in another data structure. (be vary about 1MB)
 *
 * Repeat for all the blocks in A and B. This wouldn't take more than linear time.
 *
 * m = len(A), n = len(B)
 * N = m + n
 *
 * Time complexity: O(mlogm) + O(nlogn) + O(m+n) ~ O(NlogN) [Asymptotically]
 * Space complexity: O(N)
 */
public class FileDiffsWithRamConstraints {
}
