package track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RaceTrack {

    //to build curve interval
    private static final double DEFAULT_TURN_ZONE_HALF_WIDTH = 12.0;

    private final List<TrackPoint> pathPoints;
    private final List<Double> cumulativeLengths;
    private final List<Obstacle> obstacles;
    private final List<Curve> curves;

    public RaceTrack(List<TrackPoint> pathPoints) {
        if (pathPoints == null || pathPoints.size() < 2) {
            throw new IllegalArgumentException("Track path must contain at least 2 points.");
        }
        //using a copy for better encapsulation
        this.pathPoints = new ArrayList<>(pathPoints);
        this.cumulativeLengths = buildCumulativeLengths(this.pathPoints);
        this.obstacles = new ArrayList<>();
        this.curves = buildAutoTurnZones();
    }

    private List<Double> buildCumulativeLengths(List<TrackPoint> points) {
        List<Double> lengths = new ArrayList<>();
        lengths.add(0.0);

        double total = 0;

        for (int i = 1; i < points.size(); i++) {
            TrackPoint a = points.get(i - 1);
            TrackPoint b = points.get(i);

            double segmentLength = Math.hypot(b.getX() - a.getX(), b.getY() - a.getY()); //Euclidean distance

            if (segmentLength <= 0) {
                throw new IllegalArgumentException("Track cannot contain zero-length segments.");
            }

            total += segmentLength;
            lengths.add(total);
        }

        return lengths;
    }
    //curves are built of road break points
    private List<Curve> buildAutoTurnZones() {
        List<Curve> result = new ArrayList<>();
        //just middle points
        for (int i = 1; i < pathPoints.size() - 1; i++) {
            double turnDistance = cumulativeLengths.get(i);

            result.add(new Curve(

                    //to define  correct interval(no exception for - length)
                    Math.max(0, turnDistance - DEFAULT_TURN_ZONE_HALF_WIDTH),
                    Math.min(getLength(), turnDistance + DEFAULT_TURN_ZONE_HALF_WIDTH)
            ));
        }

        return result;
    }


    //end pint of path
    public double getLength() {
        return cumulativeLengths.get(cumulativeLengths.size() - 1);
    }
    //used unmodifiable coz after creation it must stay constant (read only)
    public List<TrackPoint> getPathPoints() {
        return Collections.unmodifiableList(pathPoints);
    }

    public List<Obstacle> getObstacles() {
        return Collections.unmodifiableList(obstacles);
    }



    public void addObstacle(Obstacle obstacle) {
        if (obstacle == null) {
            throw new IllegalArgumentException("Obstacle cannot be null.");
        }

        if (!isInsideTrack(obstacle.getPosition())) {
            throw new IllegalArgumentException("Obstacle position must be inside the track.");
        }

        obstacles.add(obstacle);
    }


    //bool
    public boolean isInsideTrack(double position) {
        return position > 0 && position < getLength();
    }

    public boolean hasFinished(double carFrontPosition) {
        return carFrontPosition >= getLength();
    }



    public Curve getEnteredCurve(double previousFrontPosition, double currentFrontPosition) {
        for (Curve curve : curves) {
            if (curve.isEntered(previousFrontPosition, currentFrontPosition)) {
                return curve;
            }
        }
        return null;
    }

    public Obstacle getPassedObstacle(double previousFrontPosition, double currentFrontPosition) {
        for (Obstacle obstacle : obstacles) {
            if (previousFrontPosition < obstacle.getPosition()
                    && currentFrontPosition >= obstacle.getPosition()) {
                return obstacle;
            }
        }
        return null;
    }





    // methods for rendering (cer obstacle ,...)
    //transforming 1D to 2D


    public TrackPose getPoseAt(double distance) {


        //b4 start
        if (distance <= 0) {
            return getPoseOnSegment(0, 0);
        }
        //after end
        if (distance >= getLength()) {
            int lastSegment = pathPoints.size() - 2;
            double localDistance = cumulativeLengths.get(lastSegment + 1) - cumulativeLengths.get(lastSegment);//LAST POINT
            return getPoseOnSegment(lastSegment, localDistance);
        }

        int segmentIndex = getSegmentIndexAt(distance);
        //distance on seg
        double localDistance = distance - cumulativeLengths.get(segmentIndex);

        return getPoseOnSegment(segmentIndex, localDistance);
    }

    private int getSegmentIndexAt(double distance) {
        for (int i = 0; i < cumulativeLengths.size() - 1; i++) {
            if (distance >= cumulativeLengths.get(i) && distance <= cumulativeLengths.get(i + 1)) {
                return i;
            }
        }

        return cumulativeLengths.size() - 2;
    }
    //return track pos with (X Y radius)
    private TrackPose getPoseOnSegment(int segmentIndex, double localDistance) {
        //start seg point
        TrackPoint a = pathPoints.get(segmentIndex);
        //end seg point
        TrackPoint b = pathPoints.get(segmentIndex + 1);

        double dx = b.getX() - a.getX();
        double dy = b.getY() - a.getY();
        double length = Math.hypot(dx, dy);
        //progress ratio
        double t = localDistance / length;
        //clamp t
        t = Math.max(0, Math.min(1, t));

        double x = a.getX() + dx * t;
        double y = a.getY() + dy * t;

        //for heading direction
        double heading = Math.atan2(dy, dx);

        return new TrackPose(x, y, heading);
    }
//for view port
    public double getMinX() {
        double min = Double.MAX_VALUE;
        for (TrackPoint point : pathPoints) {
            min = Math.min(min, point.getX());
        }
        return min;
    }

    public double getMaxX() {
        double max = -Double.MAX_VALUE;
        for (TrackPoint point : pathPoints) {
            max = Math.max(max, point.getX());
        }
        return max;
    }

    public double getMinY() {
        double min = Double.MAX_VALUE;
        for (TrackPoint point : pathPoints) {
            min = Math.min(min, point.getY());
        }
        return min;
    }

    public double getMaxY() {
        double max = -Double.MAX_VALUE;
        for (TrackPoint point : pathPoints) {
            max = Math.max(max, point.getY());
        }
        return max;
    }
}