package gui.raceview;

import track.RaceTrack;
import track.TrackPoint;

import java.awt.geom.Point2D;

public class TrackViewport {

    private final double minX;
    private final double maxY;
    private final double scale;
    private final double offsetX;
    private final double offsetY;

    public TrackViewport(RaceTrack track, int panelWidth, int panelHeight, int margin) {
        double worldMinX = track.getMinX();
        double worldMaxX = track.getMaxX();
        double worldMinY = track.getMinY();
        double worldMaxY = track.getMaxY();

        double worldWidth = Math.max(1.0, worldMaxX - worldMinX);
        double worldHeight = Math.max(1.0, worldMaxY - worldMinY);
        //to consider margin
        double availableWidth = Math.max(1.0, panelWidth - margin * 2.0);
        double availableHeight = Math.max(1.0, panelHeight - margin * 2.0);

        this.scale = Math.min(availableWidth / worldWidth, availableHeight / worldHeight);

        double usedWidth = worldWidth * scale;
        double usedHeight = worldHeight * scale;
        //TO FIT IN MIDDLE ON PANEL
        this.offsetX = (panelWidth - usedWidth) / 2.0;
        this.offsetY = (panelHeight - usedHeight) / 2.0;

        this.minX = worldMinX;
        this.maxY = worldMaxY;
    }

    public Point2D toScreenPoint(TrackPoint point) {
        return toScreenPoint(point.getX(), point.getY());
    }

    public Point2D toScreenPoint(double worldX, double worldY) {
        double x = offsetX + (worldX - minX) * scale;
        double y = offsetY + (maxY - worldY) * scale;
        return new Point2D.Double(x, y);
    }


}