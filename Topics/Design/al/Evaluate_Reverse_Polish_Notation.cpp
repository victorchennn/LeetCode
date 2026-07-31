using namespace std;

// A vector<int> can act as a stack and may be simpler:  right = values.back(); values.pop_back();
class EvaluateReversePolishNotation {
public:
    int evalRPN(const vector<string>& tokens) {
        const unordered_map<string, function<int(int, int)>> operators = {
            {"+", [](int a, int b) { return a + b; }},
            {"-", [](int a, int b) { return a - b; }},
            {"*", [](int a, int b) { return a * b; }},
            {"/", [](int a, int b) { return a / b; }}
        };

        stack<int> values;

        for (const string& token : tokens) {
            auto it = operators.find(token);
            if (it == operators.end()) {
                values.push(stoi(token));
                continue;
            }

            int right = values.top();
            values.pop();

            int left = values.top();
            values.pop();

            values.push(it->second(left, right));
        }

        return values.top();
    }
};
