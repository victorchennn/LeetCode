class Excel {
private:
    std::unordered_map<std::string, std::string> cells_;

public:
    void set(const std::string& cell, const std::string& value) {
        cells_[cell] = value;
    }

    int get(const std::string& cell) const {
        std::unordered_set<std::string> visiting;
        return evaluateCell(cell, visiting);
    }

private:
    int evaluateCell(const std::string& cell, std::unordered_set<std::string>& visiting) const {
        auto it = cells_.find(cell);

        // 未设置的单元格按 0 处理，也可以根据题意抛异常
        if (it == cells_.end()) {
            return 0;
        }

        if (visiting.contains(cell)) {
            throw std::runtime_error("Circular dependency");
        }

        visiting.insert(cell);

        const std::string& content = it->second;
        int result;

        if (!content.empty() && content[0] == '=') {
            result = evaluateFormula(content.substr(1), visiting);
        } else {
            result = std::stoi(content);
        }

        visiting.erase(cell);
        return result;
    }

    int evaluateFormula(const std::string& formula, std::unordered_set<std::string>& visiting) const {
        int result = 0;
        std::size_t start = 0;

        while (start < formula.size()) {
            std::size_t plusPosition = formula.find('+', start);

            std::string token = plusPosition == std::string::npos
                    ? formula.substr(start)
                    : formula.substr(start, plusPosition - start);

            result += evaluateToken(token, visiting);

            if (plusPosition == std::string::npos) {
                break;
            }

            start = plusPosition + 1;
        }

        return result;
    }

    int evaluateToken(const std::string& token, std::unordered_set<std::string>& visiting) const {
        if (token.empty()) {
            throw std::invalid_argument("Invalid formula");
        }

        // 数字可能是负数，因此检查第一个字符之外的内容
        std::size_t start = token[0] == '-' ? 1 : 0;
        bool isNumber = start < token.size();

        for (std::size_t i = start; i < token.size(); ++i) {
            if (!std::isdigit(static_cast<unsigned char>(token[i]))) {
                isNumber = false;
                break;
            }
        }

        if (isNumber) {
            return std::stoi(token);
        }

        // 否则认为是单元格名称，例如 A1、B23
        return evaluateCell(token, visiting);
    }
};
