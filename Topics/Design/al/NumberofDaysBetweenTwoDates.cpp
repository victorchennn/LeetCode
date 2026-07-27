class Solution {
public:
    int daysBetweenDates(string date1, string date2) {
        return abs(calcDays(date1) - calcDays(date2));
    }

private:
    const int daysPerMonth[12] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    int calcDays(const string& date) {
        int year = stoi(date.substr(0, 4));
        int month = stoi(date.substr(5, 2));
        int day = stoi(date.substr(8, 2));

        int totalDays = 0;

        for (int currentYear = 1971; currentYear < year; ++currentYear) {
            totalDays += isLeapYear(currentYear) ? 366 : 365;
        }

        for (int currentMonth = 1; currentMonth < month; ++currentMonth) {
            totalDays += daysInMonth(year, currentMonth);
        }

        totalDays += day;

        return totalDays;
    }

    bool isLeapYear(int year) {
        return year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
    }

    int daysInMonth(int year, int month) {
        if (month == 2 && isLeapYear(year)) {
            return 29;
        }
      
        return daysPerMonth[month - 1];
    }

};
