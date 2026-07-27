package track;

public class Obstacle {

    private final double position;

    public Obstacle(double position) {
        if (position <= 0) {
            throw new IllegalArgumentException("Obstacle position must be positive.");
        }
        this.position = position;
    }

    public double getPosition() {
        return position;
    }
}