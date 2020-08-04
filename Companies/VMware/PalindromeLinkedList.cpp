struct ListNode
{
  int val;
  ListNode *next;
  ListNode() : val(0), next(nullptr) {}
  ListNode(int x) : val(x), next(nullptr) {}
  ListNode(int x, ListNode *next) : val(x), next(next) {}
};

class PalindromeLinkedList
{
public:
  bool isPalindrome(ListNode *head)
  {
    if (!head || !head->next)
    {
      return true;
    }
    ListNode *slow = head, *fast = head;
    while (fast && fast->next)
    {
      slow = slow->next;
      fast = fast->next->next;
    }
    slow = reverse(slow);
    while (slow)
    {
      if (slow->val != head->val)
      {
        return false;
      }
      slow = slow->next;
      head = head->next;
    }
    return true;
  }

private:
  ListNode *reverse(ListNode *node)
  {
    ListNode *re{};
    while (node)
    {
      ListNode *temp = node->next;
      node->next = re;
      re = node;
      node = temp;
    }
    return re;
  }
};
