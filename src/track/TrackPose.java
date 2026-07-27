package track;

public class TrackPose {

    private final double x;
    private final double y;
    private final double headingRadians;

    public TrackPose(double x, double y, double headingRadians) {
        this.x = x;
        this.y = y;
        this.headingRadians = headingRadians;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getHeadingRadians() {
        return headingRadians;
    }
}