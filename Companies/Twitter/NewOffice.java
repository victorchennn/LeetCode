package Companies.Twitter;

public class NewOffice {
    private int calculateHeight(int dist, int height1, int height2 ){
        int minH = Math.min(height1, height2);
        int maxH = Math.max(height1, height2);
        if(dist == 0) return 0;
        if(dist == 1) return minH + 1;
        if(minH == maxH) {
            int add = (dist%2 == 0)? dist/2 : dist/2+1;
            return minH + add;
        }
        int delta = maxH-minH;
        if(delta < dist) {
            dist -= delta;
            minH += delta;
            int add = (dist%2 == 0)? dist/2 : dist/2+1;
            return minH+add;
        }
        return minH+dist;
    }

    int getMaxHeight(int[] positions, int[] heights) {
        if(positions == null || heights == null || positions.length != heights.length) {
            return 0;
        }
        int result = 0;
        for(int i = 1; i < positions.length; ++i) {
            int currMax = calculateHeight( positions[i]-positions[i-1]-1, heights[i], heights[i-1] );
            result = Math.max(result, currMax);
        }
        return result;
    }

    public static void main(String...args) {
        NewOffice test = new NewOffice();
        System.out.println(test.getMaxHeight(new int[]{1,10}, new int[]{1,5}));
        System.out.println(test.getMaxHeight(new int[]{1,3,7}, new int[]{4,3,3}));
    }
}
