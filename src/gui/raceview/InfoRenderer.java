package gui.raceview;

import car.Car;

import java.awt.*;

public class InfoRenderer {

    public void drawInfo(Graphics2D g2, Car playerCar, Car opponentCar) {
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 14));

        g2.drawString("Player Speed: " + format(playerCar.getCurrentSpeed()), 60, 30);
        g2.drawString("Player Fuel: " + format(playerCar.getCurrentFuel()), 60, 50);
        g2.drawString("Player Mode: " + (playerCar.isFastModeEnabled() ? "FAST" : "SAFE"), 60, 70);

        g2.drawString("Opponent Car: " + opponentCar.getClass().getSimpleName(), 330, 30);
        g2.drawString("Opponent Driver: " + opponentCar.getDriver().getClass().getSimpleName(), 330, 50);
        g2.drawString("Opponent Speed: " + format(opponentCar.getCurrentSpeed()), 330, 70);
        g2.drawString("Opponent Fuel: " + format(opponentCar.getCurrentFuel()), 330, 90);
    }

    private String format(double number) {
        return String.format("%.2f", number);
    }
}