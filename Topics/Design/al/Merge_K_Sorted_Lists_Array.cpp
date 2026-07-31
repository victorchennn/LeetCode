vector<int> mergeKArrays(const vector<vector<int>>& arrays) {
    using Node = tuple<int, int, int>; // value, array index, element index
    priority_queue<Node, vector<Node>, greater<Node>> pq;

    for (int i = 0; i < arrays.size(); ++i)
        if (!arrays[i].empty())
            pq.push({arrays[i][0], i, 0});

    vector<int> result;

    while (!pq.empty()) {
        auto [value, arrayIdx, idx] = pq.top();
        pq.pop();

        result.push_back(value);

        if (idx + 1 < arrays[arrayIdx].size())
            pq.push({arrays[arrayIdx][idx + 1], arrayIdx, idx + 1});
    }

    return result;
}

class Solution {
public:
    ListNode* mergeKLists(vector<ListNode*>& lists) {
        auto cmp = [](ListNode* a, ListNode* b) {
            return a->val > b->val;
        };

        priority_queue<ListNode*, vector<ListNode*>, decltype(cmp)> pq(cmp);

        for (ListNode* node : lists) {
            if (node) {
                pq.push(node);
            }
        }

        ListNode dummy;
        ListNode* tail = &dummy;

        while (!pq.empty()) {
            ListNode* cur = pq.top();
            pq.pop();

            tail->next = cur;
            tail = tail->next;

            if (cur->next) {
                pq.push(cur->next);
            }
        }

        return dummy.next;
    }
};

class Solution {
public:
    ListNode* mergeKLists(vector<ListNode*>& lists) {
        if (lists.empty()) {
            return nullptr;
        }
        return mergeSort(lists, 0, lists.size() - 1);
    }

private:
    ListNode* mergeSort(vector<ListNode*>& lists, int left, int right) {
        if (left == right) {
            return lists[left];
        }

        int mid = left + (right - left) / 2;

        ListNode* l1 = mergeSort(lists, left, mid);
        ListNode* l2 = mergeSort(lists, mid + 1, right);

        return merge(l1, l2);
    }

    ListNode* merge(ListNode* l1, ListNode* l2) {
        ListNode dummy;
        ListNode* tail = &dummy;

        while (l1 && l2) {
            if (l1->val < l2->val) {
                tail->next = l1;
                l1 = l1->next;
            } else {
                tail->next = l2;
                l2 = l2->next;
            }
            tail = tail->next;
        }

        tail->next = l1 ? l1 : l2;
        return dummy.next;
    }
};
