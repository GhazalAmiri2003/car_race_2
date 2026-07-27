package gui.raceview;

import java.awt.*;

public class CountdownRenderer {

    public void drawCountdown(Graphics2D g2, String countdownText, int panelWidth) {
        if (countdownText == null || countdownText.isBlank()) {
            return;
        }

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 60));

        FontMetrics fm = g2.getFontMetrics();
        //چون می‌خوای متن رو وسط پنل قرار بدی.
        int textWidth = fm.stringWidth(countdownText);

        g2.drawString(countdownText, (panelWidth - textWidth) / 2, 80);
    }
}