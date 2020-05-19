package Companies.Bloomberg;

import java.util.List;

public class ValidWordSquare {
    public boolean validWordSquare(List<String> words) {
//        for(int i = 0; i < words.size(); i++){
//            for(int j = 0; j < words.get(i).length(); j++){
//                if(j >= words.size() || words.get(j).length() <= i || words.get(j).charAt(i) != words.get(i).charAt(j))
//                    return false;
//            }
//        }
//        return true;


         try {
             for (int i = 0; i < words.size(); i++) {
                 for (int j = 0; j < words.get(i).length(); j++) {
                     if (words.get(i).charAt(j) != words.get(j).charAt(i)) {
                         return false;
                     }
                 }
             }
             return true;
         }
         catch (RuntimeException e) {
             return false;
         }
    }
}
