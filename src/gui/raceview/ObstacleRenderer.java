package gui.raceview;

import track.Obstacle;
import track.RaceTrack;
import track.TrackPose;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class ObstacleRenderer {

    public void drawObstacles(Graphics2D g2,
                              RaceTrack track,
                              TrackViewport viewport,
                              double playerLaneOffset,
                              double opponentLaneOffset) {

        for (Obstacle obstacle : track.getObstacles()) {
            drawObstacleInLane(g2, track, viewport, obstacle, playerLaneOffset);
            drawObstacleInLane(g2, track, viewport, obstacle, opponentLaneOffset);
        }
    }

    private void drawObstacleInLane(Graphics2D g2,
                                    RaceTrack track,
                                    TrackViewport viewport,
                                    Obstacle obstacle,
                                    double laneOffset) {

        TrackPose pose = track.getPoseAt(obstacle.getPosition());
        double heading = pose.getHeadingRadians();

        double nx = -Math.sin(heading);
        double ny = Math.cos(heading);

        double worldX = pose.getX() + nx * laneOffset;
        double worldY = pose.getY() + ny * laneOffset;

        Point2D screen = viewport.toScreenPoint(worldX, worldY);

        AffineTransform old = g2.getTransform();

        g2.translate(screen.getX(), screen.getY());
        g2.rotate(-heading);

        g2.setColor(new Color(120, 30, 30));
        g2.fillRect(-6, -8, 12, 16);

        g2.setColor(new Color(170, 60, 60));
        g2.fillRect(-4, -6, 8, 12);

        g2.setTransform(old);
    }
}