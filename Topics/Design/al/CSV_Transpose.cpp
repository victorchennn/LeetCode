// vector<string> input = {
//     "a,b,c",
//     "1,2",
//     "x,y,z,w"
// };
// auto ans = transposeCSV(input);
// a,1,x
// b,2,y
// c,,z
// ,,w

vector<string> transposeCSV(const vector<string>& lines) {
    vector<vector<string>> columns;

    for (int row = 0; row < lines.size(); ++row) {
        stringstream ss(lines[row]);
        string value;
        int col = 0;

        while (getline(ss, value, ',')) {
            if (col == columns.size())
                columns.emplace_back(row, "");

            columns[col].push_back(value);
            ++col;
        }

        while (col < columns.size())
            columns[col++].push_back("");
    }

    vector<string> result;
    for (const auto& column : columns) {
        string line;
        for (int i = 0; i < column.size(); ++i) {
            if (i) line += ",";
            line += column[i];
        }
        result.push_back(line);
    }

    return result;
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
