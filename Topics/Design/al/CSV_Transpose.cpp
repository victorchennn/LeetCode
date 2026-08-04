void transposeCSV(istream& in, ostream& out) {
    vector<vector<string>> columns;
    string line;
    size_t rowCount = 0;

    while (getline(in, line)) {
        stringstream ss(line);
        string value;
        size_t col = 0;

        while (getline(ss, value, ',')) {
            if (col == columns.size()) {
                // 新列之前的行都缺少该列
                columns.emplace_back(rowCount, "");
            }

            columns[col].push_back(value);
            ++col;
        }

        // 当前行没有的列补空字符串
        while (col < columns.size()) {
            columns[col].push_back("");
            ++col;
        }

        ++rowCount;
    }

    for (const auto& column : columns) {
        for (size_t i = 0; i < column.size(); ++i) {
            if (i > 0) {
                out << ',';
            }
            out << column[i];
        }
        out << '\n';
    }
}

// return这个column里面所有value
// 给一个 CSV 文件，例如：
// Name,Age,City
// Alice,20,NY
// Bob,30,SF
// Charlie,25,LA
class CSVReader {
public:
    explicit CSVReader(istream& in) {
        string line;

        // header
        getline(in, line);
        data_.push_back(split(line));

        for (int i = 0; i < data_[0].size(); ++i) {
            index_[data_[0][i]] = i;
        }

        while (getline(in, line)) {
            data_.push_back(split(line));
        }
    }

    vector<string> getColumn(const string& name) {
        vector<string> result;

        auto it = index_.find(name);
        if (it == index_.end()) {
            return result;
        }

        int col = it->second;

        for (int i = 1; i < data_.size(); ++i) {
            if (col < data_[i].size()) {
                result.push_back(data_[i][col]);
            }
        }

        return result;
    }

private:
    vector<vector<string>> data_;
    unordered_map<string, int> index_;

    vector<string> split(const string& line) {
        vector<string> fields;
        string field;
        stringstream ss(line);

        while (getline(ss, field, ',')) {
            fields.push_back(field);
        }

        return fields;
    }
};
