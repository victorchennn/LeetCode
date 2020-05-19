package Companies.Twitter;

public class MinimumNumberofStepstoMakeTwoStringsAnagram {
    public int minSteps(String s, String t) {
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c-'a']--;
        }
        for (char c : t.toCharArray()) {
            count[c-'a']++;
        }
        int step = 0;
        for (int c : count) {
            step += Math.max(0, c);
        }
        return step;
    }
}
