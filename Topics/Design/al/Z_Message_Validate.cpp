enum class MessageType {
    Instrument,
    Order,
    Execution
};

struct Message {
    MessageType type;

    string id;            // 当前 message 自己的 id
    string instrumentId;  // Order / Execution 使用
    string orderId;       // Execution 使用
};

struct ValidationResult {
    bool valid;
    vector<string> errors;
};

class MessageValidator {
private:
    struct OrderInfo {
        string instrumentId;
    };

    // 已经成功处理过的 instrument
    unordered_map<string, Message> instruments_;

    // 已经成功处理过的 order
    unordered_map<string, OrderInfo> orders_;

public:
    ValidationResult validate(const Message& message) {
        switch (message.type) {
            case MessageType::Instrument:
                return validateInstrument(message);

            case MessageType::Order:
                return validateOrder(message);

            case MessageType::Execution:
                return validateExecution(message);
        }

        return {false, {"Unknown message type"}};
    }

private:
    ValidationResult validateInstrument(const Message& message) {
        // 题目说 Instrument 永远 valid
        instruments_[message.id] = message;
        return {true, {}};
    }

    ValidationResult validateOrder(const Message& message) {
        vector<string> errors;

        if (!instruments_.contains(message.instrumentId)) {
            errors.push_back(
                "Instrument does not exist: " + message.instrumentId
            );
        }

        bool valid = errors.empty();

        // 只有合法 order 才进入状态
        if (valid) {
            orders_[message.id] = {message.instrumentId};
        }

        return {valid, std::move(errors)};
    }

    ValidationResult validateExecution(const Message& message) {
        vector<string> errors;

        auto instrumentIt = instruments_.find(message.instrumentId);
        auto orderIt = orders_.find(message.orderId);

        // 两个引用分别检查，不能使用 else-if
        if (instrumentIt == instruments_.end()) {
            errors.push_back(
                "Instrument does not exist: " + message.instrumentId
            );
        }

        if (orderIt == orders_.end()) {
            errors.push_back(
                "Order does not exist: " + message.orderId
            );
        }

        // 只有 order 存在时，才能进一步检查 instrument 是否匹配
        if (orderIt != orders_.end() &&
            orderIt->second.instrumentId != message.instrumentId) {
            errors.push_back(
                "Instrument mismatch: execution references instrument " +
                message.instrumentId +
                ", but order " + message.orderId +
                " references instrument " +
                orderIt->second.instrumentId
            );
        }

        return {errors.empty(), std::move(errors)};
    }
};
