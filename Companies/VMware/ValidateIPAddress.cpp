#include <iostream>
#include <string>
#include <algorithm>
#include <vector>
#include <sstream>

using namespace std;

class ValidateIPAddress {
public:
    string validIPAddress(string IP) {
        return validIPv4(IP) ? "IPv4" : (validIPv6(IP) ? "IPv6" : "Neither");
    }

private:
    bool validIPv4(string IP) {
        if (count(IP.begin(), IP.end(), '.') != 3) {
            return false;
        }
        vector<string> v4 = split(IP, '.');
        if (v4.size() != 4) {
            return false;
        }
        for (string p : v4) {
            if (p.empty() || p.size() > 3 || p.size() > 1 && p[0] == '0') {
                return false;
            }
            for (char c : p) {
                if (!isdigit(c)) {
                    return false;
                }
            }
            if (stoi(p) > 255) {
                return false;
            }
        }
        return true;
    }

    bool validIPv6(string IP) {
        if (count(IP.begin(), IP.end(), ':') != 7) {
            return false;
        }
        vector<string> v6 = split(IP, ':');
        if (v6.size() != 8) {
            return false;
        }
        for (string p : v6) {
            if (p.empty() || p.size() > 4) {
                return false;
            }
            for (char c : p) {
                if (!isxdigit(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    vector<string> split(string s, char c) {
        vector<string> re{};
        string part{};
        istringstream in{ s };
        while (getline(in, part, c)) {
            re.push_back(part);
        }
        return re;
    }
};