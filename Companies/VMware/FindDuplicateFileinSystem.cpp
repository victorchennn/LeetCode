#include <iostream>
#include <string>
#include <vector>
#include <unordered_map>
#include <sstream>

using namespace std;

class FindDuplicateFileinSystem
{
public:
  vector<vector<string>> findDuplicate(vector<string> &paths)
  {
    unordered_map<string, vector<string>> files{};
    for (auto path : paths)
    {
      stringstream ps{path};
      string root{}, s{};
      getline(ps, root, ' ');
      while (getline(ps, s, ' '))
      {
        string name = root + '/' + s.substr(0, s.find('('));
        string content = s.substr(s.find('(') + 1, s.find(')') - s.find('(') - 1);
        files[content].push_back(name);
      }
    }
    vector<vector<string>> re{};
    for (auto file : files)
    {
      if (file.second.size() > 1)
      {
        re.push_back(file.second);
      }
    }
    return re;
  }
};