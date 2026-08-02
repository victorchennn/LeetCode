// Given a string of gene, in the form of {A, C, G, T}, 
// return the minimal length of the substring after changing characters in which（
// can change some characters of them not all of them）so that the result string has equal number of genes.

// e.g ["AAAA"]  返回3, 后三个"AAA"改成"CGT"
// e.g‍‍‌‍‌‍‍‍‍‌‌‌‌‍‌‍‍‍‌‍ ["ACCCGTTT"]  返回3, 改动"CGT" 成为 "AGG"
  
class Solution {
private:
    int index(char c) {
        if (c == 'A') return 0;
        if (c == 'C') return 1;
        if (c == 'G') return 2;
        return 3;  // T
    }

    bool valid(const array<int, 4>& outside, int target) {
        for (int count : outside) {
            if (count > target) {
                return false;
            }
        }
        return true;
    }

public:
    int minReplaceLength(const string& gene) {
        int n = gene.size();
        int target = n / 4;

        // count 表示当前窗口外的字符数量
        array<int, 4> outside{};

        for (char c : gene) {
            ++outside[index(c)];
        }

        // 已经平衡，不需要修改
        if (valid(outside, target)) {
            return 0;
        }

        int answer = n;
        int left = 0;

        for (int right = 0; right < n; ++right) {
            // gene[right] 进入窗口，因此从窗口外删除
            --outside[index(gene[right])];

            // 外部已经合法，尝试缩小窗口
            while (left <= right && valid(outside, target)) {
                answer = min(answer, right - left + 1);

                // gene[left] 离开窗口，重新回到窗口外
                ++outside[index(gene[left])];
                ++left;
            }
        }

        return answer;
    }
};
