package gui.raceview;

import race.RaceEventLog;

import java.awt.*;
import java.util.List;

public class EventRenderer {

    public void drawFuelIcon(Graphics2D g2, int x, int y) {
        g2.setColor(Color.BLACK);
        g2.drawRect(x, y, 18, 10);
        g2.fillRect(x + 18, y + 3, 3, 4);

        g2.setColor(Color.RED);
        g2.fillRect(x + 2, y + 2, 6, 6);
    }

    public void drawCrashIcon(Graphics2D g2, int x, int y) {
        g2.setColor(Color.RED);
        //خطوط ضربدر رو ضخیم‌تر می‌کنه.
        g2.setStroke(new BasicStroke(3));
        //ک ضربدر X درست می‌کنن.
        g2.drawLine(x, y, x + 16, y + 16);
        g2.drawLine(x + 16, y, x, y + 16);
        g2.drawString("CRASH", x - 2, y - 6);
    }

    public void drawEventLogs(Graphics2D g2, List<RaceEventLog> logs, int x, int y) {
        if (logs == null || logs.isEmpty()) {
            return;
        }

        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 13));

        int lineY = y;

        for (int i = logs.size() - 1; i >= 0; i--) {
            RaceEventLog log = logs.get(i);
            g2.drawString("Event: " + log.getMessage(), x, lineY);
            lineY -= 18;
        }
    }
}