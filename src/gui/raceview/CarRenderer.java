package gui.raceview;

import car.Car;
import car.StopReason;
import track.RaceTrack;
import track.TrackPose;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

public class CarRenderer {

    private final EventRenderer eventRenderer;

    public CarRenderer(EventRenderer eventRenderer) {
        if (eventRenderer == null) {
            throw new IllegalArgumentException("EventRenderer cannot be null.");
        }
        this.eventRenderer = eventRenderer;
    }

    public void drawCar(Graphics2D g2,
                        Car car,
                        RaceTrack track,//FOR CAR POS
                        TrackViewport viewport,
                        double laneOffset, //TO SEPARATE CARS IN PANEL
                        Color color,
                        String label) {

        if (car == null) {
            throw new IllegalArgumentException("Car cannot be null.");
        }

        TrackPose pose = track.getPoseAt(car.getCurrentPosition()); //1D TO 2D
        double heading = pose.getHeadingRadians();


        //2 PERPENDICULAR VECTORS TO HEADING
        double nx = -Math.sin(heading);
        double ny = Math.cos(heading);
        //از مرکز مسیر، به اندازه‌ی laneOffset در جهت عمود جابه‌جا شو
        double worldX = pose.getX() + nx * laneOffset;
        double worldY = pose.getY() + ny * laneOffset;
        //FOR DRAW ON PANEL BASED ON GAME LOC
        Point2D screen = viewport.toScreenPoint(worldX, worldY);


// jump height taken from car state
        int jumpOffset = car.getJumpOffsetY();

// shadow stays on the ground while jumping
        drawShadow(g2, screen.getX(), screen.getY());


        //rotate after curve
// save current drawing transform
        AffineTransform old = g2.getTransform();

// move drawing origin to car position
        g2.translate(screen.getX(), screen.getY() - jumpOffset);

// rotate drawing so the car matches road direction
        g2.rotate(-heading);

        drawBody(g2, color);
        drawLabel(g2, label);

// restore previous drawing transform
        g2.setTransform(old);

        if (car.isJumping()) {
            g2.setColor(new Color(20, 90, 20));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            //متن JUMP رو کمی بالاتر از ماشین بکش
            g2.drawString("JUMP", (int) screen.getX() - 16, (int) screen.getY() - 28 - jumpOffset);
        }

        if (car.isStopped()) {
            if (car.getStopReason() == StopReason.FUEL) {
                eventRenderer.drawFuelIcon(g2, (int) screen.getX() - 12, (int) screen.getY() - 40);
            } else if (car.getStopReason() == StopReason.CRASH) {
                eventRenderer.drawCrashIcon(g2, (int) screen.getX() - 10, (int) screen.getY() - 38);
            }

            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString("STOP " + car.getRemainingStopTicks(),
                    (int) screen.getX() - 22,
                    (int) screen.getY() + 34);
        }

        if (car.isIgnoringHazards()) {
            g2.setColor(new Color(20, 120, 20));
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            g2.drawString("RECOVER", (int) screen.getX() - 26, (int) screen.getY() - 48);
        }
    }

    private void drawShadow(Graphics2D g2, double x, double y) {
        g2.setColor(new Color(0, 0, 0, 60));
        g2.fillOval((int) x - 16, (int) y + 10, 32, 10);
    }

    private void drawBody(Graphics2D g2, Color color) {

        g2.setColor(color);
        //چون می‌خواد این بدنه حول مرکز (0,0) قرار بگیره:
        g2.fillRoundRect(-18, -10, 36, 20, 8, 8);
        //باعث میشه ماشین تخت و ساده دیده نشه و کمی حجم بگیره.
        g2.setColor(color.brighter());
        g2.fillRoundRect(-10, -16, 20, 8, 6, 6);
        //چرخ ها
        g2.setColor(Color.BLACK);
        g2.fillOval(-14, 8, 8, 8);
        g2.fillOval(6, 8, 8, 8);
    }

    private void drawLabel(Graphics2D g2, String label) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.drawString(label, -16, -18);
    }
}