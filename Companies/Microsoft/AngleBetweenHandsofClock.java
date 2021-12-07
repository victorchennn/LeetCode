package Companies.Microsoft;

public class AngleBetweenHandsofClock {
    public double angleClock(int hour, int minutes) {
        // Degree covered by hour hand (hour area + minutes area)
        double h = (hour%12 * 30) + ((double)minutes/60 * 30);

        // Degree covered by minute hand (Each minute = 6 degree)
        double m = minutes * 6;

        // Absolute angle between them
        double angle = Math.abs(m - h);

        // If the angle is obtuse (>180), convert it to acute (0<=x<=180)
        if (angle > 180) angle = 360.0 - angle;

        return angle;
    }
}
