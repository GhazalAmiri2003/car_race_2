package gui.raceview;

import track.RaceTrack;
import track.TrackPoint;

import java.awt.*;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.util.List;

public class RoadRenderer {

    public void drawRoad(Graphics2D g2, RaceTrack track, TrackViewport viewport) {
        Path2D path = buildScreenPath(track.getPathPoints(), viewport);

        Stroke oldStroke = g2.getStroke();  //ضخامت/سبک خط فعلی رو ذخیره کن.

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(58f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);

        g2.setColor(new Color(85, 85, 85));
        g2.setStroke(new BasicStroke(48f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.draw(path);

        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(
                3f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_ROUND,
                10f,
                new float[]{24f, 18f},
                0f
        ));
        g2.draw(path);

        drawStartMarker(g2, track, viewport);
        drawFinishMarker(g2, track, viewport);

        g2.setStroke(oldStroke);      //ضخامت/سبک خط رو به حالت قبلی برگردون
    }

    private Path2D buildScreenPath(List<TrackPoint> points, TrackViewport viewport) {
        Path2D path = new Path2D.Double();     //یک مسیر گرافیکی خالی درست کن

        Point2D first = viewport.toScreenPoint(points.get(0));
        path.moveTo(first.getX(), first.getY());

        for (int i = 1; i < points.size(); i++) {
            Point2D p = viewport.toScreenPoint(points.get(i));
            path.lineTo(p.getX(), p.getY());
        }

        return path;
    }

    private void drawStartMarker(Graphics2D g2, RaceTrack track, TrackViewport viewport) {
        drawPerpendicularMarker(g2, track, viewport, 0, Color.YELLOW, 6);
    }

    private void drawFinishMarker(Graphics2D g2, RaceTrack track, TrackViewport viewport) {
        drawPerpendicularMarker(g2, track, viewport, track.getLength(), Color.BLACK, 10);
        drawPerpendicularMarker(g2, track, viewport, track.getLength() - 4, Color.WHITE, 10);
    }

    private void drawPerpendicularMarker(Graphics2D g2,
                                         RaceTrack track,
                                         TrackViewport viewport,
                                         double distance,
                                         Color color,
                                         int thickness) {
        var pose = track.getPoseAt(distance);
        double heading = pose.getHeadingRadians();

        double nx = -Math.sin(heading);
        double ny = Math.cos(heading);

        double half = 26.0;

        Point2D a = viewport.toScreenPoint(
                pose.getX() + nx * half,
                pose.getY() + ny * half
        );

        Point2D b = viewport.toScreenPoint(
                pose.getX() - nx * half,
                pose.getY() - ny * half
        );

        Stroke old = g2.getStroke();
        g2.setColor(color);
        g2.setStroke(new BasicStroke(thickness));
        g2.drawLine((int) a.getX(), (int) a.getY(), (int) b.getX(), (int) b.getY());
        g2.setStroke(old);
    }
}