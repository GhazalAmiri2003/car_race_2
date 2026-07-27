package track;

public class Curve {

    private final double startPosition;
    private final double endPosition;
    private final CurveDirection direction;

    public Curve(double startPosition, double endPosition) {
        this(startPosition, endPosition, CurveDirection.UP);
    }

    public Curve(double startPosition, double endPosition, CurveDirection direction) {
        if (startPosition < 0) {
            throw new IllegalArgumentException("Curve start position cannot be negative.");
        }

        if (endPosition <= startPosition) {
            throw new IllegalArgumentException("Curve end position must be greater than start position.");
        }

        if (direction == null) {
            throw new IllegalArgumentException("Curve direction cannot be null.");
        }

        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.direction = direction;
    }


    public CurveDirection getDirection() {
        return direction;
    }
//no need for it in final design
    public boolean isExited(double previousPosition, double currentPosition) {
        return previousPosition <= endPosition && currentPosition > endPosition;
    }

    public boolean isEntered(double previousPosition, double currentPosition) {         //TO CHECK JUST ONCE
        return previousPosition < startPosition && currentPosition >= startPosition;
    }
}