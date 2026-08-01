void PrintMessage(const string& input) {
    size_t i = 0;

    while (i < input.size()) {
        int len = stoi(input.substr(i, 2));
        i += 2;
        cout << input.substr(i, len);
        i += len;
    }
  
    cout << endl;
}

int main() {
    PrintMessage("02bc101234567890");
}

// stream?
class MessageParser {
private:
    string buffer_;

public:
     void PrintMessage(const std::string& chunk) {
        buffer_ += chunk;
        size_t pos = 0;
        while (true) {
            if (buffer_.size() - pos < 2)
                break;

            int len = std::stoi(buffer_.substr(pos, 2));

            if (buffer_.size() - pos < 2 + len)
                break;

            std::cout << buffer_.substr(pos + 2, len);
            pos += 2 + len;
        }

        buffer_.erase(0, pos); // 如果不删除，也不维护 head，程序就会一直重复解析第一条消息。
    }
};
