// "07:05:45PM" -> "19:05:45"
class Solution {
public:
    string convertTo24Hour(string s) {
        int hour = stoi(s.substr(0, 2));
        string period = s.substr(8, 2);

        if (period == "AM") {
            if (hour == 12) {
                hour = 0;
            }
        } else { // PM
            if (hour != 12) {
                hour += 12;
            }
        }

        string result;

        if (hour < 10) {
            result += "0";
        }
        result += to_string(hour);

        result += s.substr(2, 6); // ":mm:ss"

        return result;
    }
};

#include <iomanip>
#include <sstream>

std::tm tm = {};
std::istringstream iss("07:05:45PM");
iss >> std::get_time(&tm, "%I:%M:%S%p");

std::ostringstream oss;
oss << std::put_time(&tm, "%H:%M:%S");

std::cout << oss.str(); // 19:05:45
