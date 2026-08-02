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

int main() {
    transposeCSV(cin, cout);
    return 0;
}
