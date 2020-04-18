package Companies.Microsoft;

public class RotateImage {
    public void rotate(int[][] matrix) {
        int len = matrix.length;
        for (int i = 0; i < len/2; i++) {
            for (int j = i; j < len-1-i; j++) {
                int temp1 = matrix[i][len-1-j];
                matrix[i][len-1-j] = matrix[j][i];
                int temp2 = matrix[len-1-j][len-1-i];
                matrix[len-1-j][len-1-i] = temp1;
                temp1 = matrix[len-1-i][j];
                matrix[len-1-i][j] = temp2;
                matrix[j][i] = temp1;
            }
        }
    }
}
