class Solution {
public:
    double findMedianSortedArrays(vector<int>& nums1, vector<int>& nums2) {
        int total = nums1.size() + nums2.size();

        int left = findKth(nums1, 0, nums2, 0, (total + 1) / 2);
        int right = findKth(nums1, 0, nums2, 0, (total + 2) / 2);

        return (left + right) / 2.0;
    }

private:
    int findKth(const vector<int>& nums1, int i,
                const vector<int>& nums2, int j,
                int k) {
        // nums1 已经用完
        if (i >= nums1.size()) {
            return nums2[j + k - 1];
        }

        // nums2 已经用完
        if (j >= nums2.size()) {
            return nums1[i + k - 1];
        }

        // 找最小值
        if (k == 1) {
            return min(nums1[i], nums2[j]);
        }

        int half = k / 2;

        int value1 = (i + half - 1 < nums1.size())
                         ? nums1[i + half - 1]
                         : INT_MAX;

        int value2 = (j + half - 1 < nums2.size())
                         ? nums2[j + half - 1]
                         : INT_MAX;

        if (value1 < value2) {
            return findKth(nums1, i + half,
                           nums2, j,
                           k - half);
        } else {
            return findKth(nums1, i,
                           nums2, j + half,
                           k - half);
        }
    }
};
